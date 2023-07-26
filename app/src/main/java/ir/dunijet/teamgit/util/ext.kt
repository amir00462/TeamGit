package ir.dunijet.teamgit.util

fun String.toParagraph(size: Int): String {

    val stringBuilder = StringBuilder()
    val lines = this.split(". ")

    var currentParagraph = ""
    var lineCount = 0
    for (line in lines) {
        val trimmedLine = line.trim()
        if (trimmedLine.isNotEmpty()) {
            currentParagraph += if (currentParagraph.isNotEmpty()) {
                " $trimmedLine"
            } else {
                trimmedLine
            }
        }

        lineCount++
        if (lineCount >= size) {
            stringBuilder.append(currentParagraph).append(".\n\n")
            currentParagraph = ""
            lineCount = 0
        }
    }

    if (currentParagraph.isNotEmpty()) {
        stringBuilder.append(currentParagraph)
    }

    return stringBuilder.toString()
}