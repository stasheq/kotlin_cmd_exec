package me.szymanski.kotlinexec

internal object CommandParser {

    fun String.toParts(): List<String> {
        val parts = ArrayList<String>()
        val currentPart = StringBuilder()
        var inQuote = false
        var whiteSpaceBefore = false
        var continuingWithQuotes = false
        for (i in this.indices) {
            val c = this[i]
            when {
                c.isWhitespace() && !inQuote -> parts.addNonEmpty(currentPart.clearAndGet())
                c == '"' -> {
                    inQuote = !inQuote
                    if (inQuote && !whiteSpaceBefore) {
                        currentPart.append(c)
                        continuingWithQuotes = true
                    }
                    if (!inQuote) {
                        if (continuingWithQuotes) {
                            currentPart.append(c)
                            continuingWithQuotes = false
                        }
                        parts.add(currentPart.clearAndGet())
                    }
                }
                else -> currentPart.append(c)
            }

            whiteSpaceBefore = c.isWhitespace()
        }
        if (inQuote || continuingWithQuotes) throw IllegalArgumentException("Wrong command: $this")
        parts.addNonEmpty(currentPart.toString())
        return parts
    }

    fun List<String>.printCommand(limit: Int = -1): String =
        joinToString(" ", limit = limit) {
            if (it.contains(whiteCharsRegex)) "\"$it\"" else it
        }

    private val whiteCharsRegex = "\\s".toRegex()

    private fun ArrayList<String>.addNonEmpty(text: String) {
        if (text.isNotEmpty()) add(text)
    }

    private fun StringBuilder.clearAndGet(): String = toString().also { clear() }
}
