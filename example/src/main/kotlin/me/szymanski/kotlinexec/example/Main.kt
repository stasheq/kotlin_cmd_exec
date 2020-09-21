package me.szymanski.kotlinexec.example

import me.szymanski.kotlinexec.exec
import java.lang.Exception
import kotlin.system.exitProcess

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            println("Received arguments: ${args.joinToString(", ")}")
            "echo \"Hello world\"".exec()
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
            exitProcess(SCRIPT_FAILED_CODE)
        }
    }

    private const val SCRIPT_FAILED_CODE = -1
}
