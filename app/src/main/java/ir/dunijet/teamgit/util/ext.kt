package ir.dunijet.teamgit.util

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
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

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp,
    tabHeight: Dp,
    topCornerRadius: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
        .height(tabHeight)
        .clip( RoundedCornerShape( topStart = topCornerRadius , topEnd = topCornerRadius ) )

}
