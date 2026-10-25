package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.bekacodes.cinepulsetv.ui.TvNavTab
import com.bekacodes.cinepulsetv.ui.theme.TvEmerald
import com.bekacodes.cinepulsetv.ui.theme.TvIndigo
import com.bekacodes.cinepulsetv.ui.theme.TvInk
import com.bekacodes.cinepulsetv.ui.theme.TvMuted
import com.bekacodes.cinepulsetv.ui.theme.TvRail
import com.bekacodes.cinepulsetv.ui.theme.TvSky

@Composable
fun NavigationRail(
    selectedTab: TvNavTab,
    watchlistCount: Int,
    onSelectTab: (TvNavTab) -> Unit
) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .fillMaxHeight()
            .background(TvRail)
            .border(BorderStroke(1.dp, Color(0x14FFFFFF))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "✦",
            color = TvIndigo,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(32.dp))

        listOf(TvNavTab.HOME, TvNavTab.AI_VIBE, TvNavTab.WATCHLIST).forEach { tab ->
            var isFocused by remember { mutableStateOf(false) }
            val active = selectedTab == tab

            Box(
                modifier = Modifier
                    .size(width = 64.dp, height = 52.dp)
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusable()
                    .clickable { onSelectTab(tab) },
                contentAlignment = Alignment.Center
            ) {
                if (active || isFocused) {
                    Box(
                        Modifier
                            .align(Alignment.CenterStart)
                            .width(3.dp)
                            .height(if (isFocused) 30.dp else 22.dp)
                            .clip(CircleShape)
                            .background(if (isFocused) TvSky else TvIndigo)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isFocused -> TvIndigo.copy(alpha = 0.28f)
                                active -> TvIndigo.copy(alpha = 0.14f)
                                else -> Color.Transparent
                            }
                        )
                        .border(
                            width = if (isFocused) 1.5.dp else 0.dp,
                            color = if (isFocused) TvSky else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.iconGlyph,
                        color = if (active || isFocused) TvInk else TvMuted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (tab == TvNavTab.WATCHLIST && watchlistCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(3.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(TvEmerald)
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }

        Spacer(Modifier.weight(1f))
        Text("⚙", color = TvMuted, fontSize = 18.sp)
        Spacer(Modifier.height(24.dp))
    }
}