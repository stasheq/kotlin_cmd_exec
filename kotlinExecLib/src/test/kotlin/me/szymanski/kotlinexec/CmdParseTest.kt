package me.szymanski.kotlinexec

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import me.szymanski.kotlinexec.CommandParser.printCommand
import me.szymanski.kotlinexec.CommandParser.toParts


class CmdParseTest : FreeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    fun String.test(vararg expectedParts: String) {
        val cmd = this
        val expected = expectedParts.toList()
        cmd - {
            val parts = cmd.toParts()
            val print = parts.printCommand()
            "parts: ${expected.joinToString()}" { assert(parts == expected) }
            "print: $print" { assert(cmd == print) }
        }
    }

    "ls".test("ls")
    "git status".test("git", "status")
    "echo \"Hello world\"".test("echo", "Hello world")
    "docker ps -a | grep container_name".test("docker", "ps", "-a", "|", "grep", "container_name")
})
