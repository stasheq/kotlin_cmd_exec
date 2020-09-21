package me.szymanski.kotlinexec

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import me.szymanski.kotlinexec.CommandParser.printCommand
import me.szymanski.kotlinexec.CommandParser.toParts
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

fun String.exec(
    workingDir: File? = null,
    throwOnError: Boolean = true,
    printLimit: Int = -1
): ExecOutput = toParts().exec(workingDir, throwOnError, printLimit)

fun List<String>.exec(
    workingDir: File? = null,
    throwOnError: Boolean = true,
    printLimit: Int = -1
): ExecOutput {
    println(this.printCommand(printLimit))
    val proc = ProcessBuilder(this)
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val outputBuilder = StringBuilder()
    val outputJob = scope.launch { readStream(proc.inputStream, outputBuilder, System.out) }
    val errorBuilder = StringBuilder()
    val errorJob = scope.launch { readStream(proc.errorStream, errorBuilder, System.out) }

    proc.waitFor(Long.MAX_VALUE, TimeUnit.MILLISECONDS)
    runBlocking {
        withTimeout(5000L) {
            outputJob.join()
            errorJob.join()
        }
    }

    val exitCode = proc.exitValue()
    val result = ExecOutput(
        this,
        outputBuilder.toString().trim(),
        errorBuilder.toString().trim(),
        exitCode
    )

    if (!result.isSuccess() && throwOnError) {
        throw CommandFailedException(result, printLimit)
    }
    return result
}

data class ExecOutput(
    val command: List<String>,
    val output: String,
    val error: String,
    val exitCode: Int
) {
    fun isSuccess(): Boolean = exitCode == 0
}

class CommandFailedException(result: ExecOutput, printLimit: Int = -1) :
    RuntimeException("Command '${result.command.printCommand(printLimit)}' failed with exit code ${result.exitCode}")

private suspend fun readStream(
    inputStream: InputStream,
    stringBuilder: StringBuilder,
    printer: PrintStream? = null
) = coroutineScope {
    val reader = inputStream.bufferedReader()
    while (isActive) {
        val line: String = reader.readLine() ?: break
        printer?.println(line)
        stringBuilder.append(line)
        stringBuilder.append('\n')
    }
    reader.close()
}
