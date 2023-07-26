package ir.dunijet.teamgit.util

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

@Composable
fun getCurrentOrientation(): Int {

    // 0 ->  "Portrait"
    // 1 -> "Landscape"

    val configuration = LocalConfiguration.current
    return if (configuration.screenWidthDp > configuration.screenHeightDp) {
        1
    } else {
        0
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toReadableDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val dateTime = LocalDateTime.parse(this, formatter)

    val zoneId = ZoneId.systemDefault()
    val instant = dateTime.atZone(zoneId).toInstant()
    val readableDate = DateTimeFormatter.ofPattern("yyyy/M/d").format(dateTime)
    val readableTime = DateTimeFormatter.ofPattern("H:mm").format(dateTime)

    return "$readableTime ,$readableDate"
}

fun Context.showToast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}