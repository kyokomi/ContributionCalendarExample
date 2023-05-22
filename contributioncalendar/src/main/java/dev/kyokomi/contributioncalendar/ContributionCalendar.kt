package dev.kyokomi.contributioncalendar

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Composable
fun ContributionCalendar(
    today: OffsetDateTime,
    paddingSize: Dp,
    dayOfCount: Map<Int, Int>,
) {
    val rowSize = 30
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    // 画面サイズからpadding分とBoarder分を引いて残ったサイズで30列表示するために1セルのサイズを計算する
    val cellSize = (screenWidth - (paddingSize * 2)) / (rowSize + 1)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        // TODO: GridView使えないか検討する
        for (w in (rowSize - 1) downTo 0) {
            Column {
                // 7日
                for (d in 6 downTo 0) {
                    val idx = (d + (w * 7)).toLong()
                    val day = today.minusDays(idx).truncatedTo(ChronoUnit.DAYS)
                        .plusDays((7 - today.dayOfWeek.value).toLong())

                    val color = if (today.isBefore(day)) {
                        Color.Transparent
                    } else {
                        // TODO: Material3のColorSchemeを使う
                        when (dayOfCount[day.dayOfYear]) {
                            1 -> Color(0x88880000)
                            2 -> Color(0xBBBB0000)
                            3 -> Color(0xFFFF0000)
                            else -> Color.LightGray
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .padding(1.dp)
                            .background(color),
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ContributionCalendarPreview() {
    val now = OffsetDateTime.now().minusDays(0)
    MaterialTheme {
        ContributionCalendar(
            today = now,
            paddingSize = 16.dp,
            dayOfCount = mutableMapOf(
                now.dayOfYear - 1 to 7, // 10
                now.dayOfYear - 2 to 6, // 9
                now.dayOfYear - 3 to 6, // 9
                now.dayOfYear - 4 to 5, // 7
                now.dayOfYear - 5 to 5, // 7
                now.dayOfYear - 6 to 4, // 5
                now.dayOfYear - 7 to 3, // 4
                now.dayOfYear - 8 to 2, // 3
                now.dayOfYear - 9 to 1, // 2
                now.minusYears(1).plusDays(1).dayOfYear to 1, // 2
            ),
        )
    }
}
