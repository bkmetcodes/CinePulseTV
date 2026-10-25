package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.bekacodes.cinepulsetv.ui.theme.TvIndigo
import com.bekacodes.cinepulsetv.ui.theme.TvInk
import com.bekacodes.cinepulsetv.ui.theme.TvMuted
import com.bekacodes.cinepulsetv.ui.theme.TvSky
import com.bekacodes.cinepulsetv.ui.theme.TvSurface

@Composable
fun SearchBar(
    activeQuery: String,
    resultCount: Int,
    onClickSearch: () -> Unit,
    onCyclePreset: () -> Unit
) {
    var isBarFocused by remember { mutableStateOf(false) }
    var isCycleFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .shadow(if (isBarFocused) 18.dp else 10.dp, CircleShape, ambientColor = TvIndigo, spotColor = TvIndigo)
                .clip(CircleShape)
                .background(TvSurface)
                .border(
                    BorderStroke(
                        width = if (isBarFocused) 2.dp else 1.2.dp,
                        color = if (isBarFocused) TvSky else TvIndigo
                    ),
                    CircleShape
                )
                .onFocusChanged { isBarFocused = it.isFocused }
                .focusable()
                .clickable(onClick = onClickSearch)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🎙", fontSize = 15.sp)
            Text(
                text = "✦ $activeQuery",
                modifier = Modifier.weight(1f),
                color = TvInk,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "[$resultCount Results Ranked]",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(TvIndigo.copy(alpha = 0.18f))
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                color = TvInk,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.width(12.dp))

        // Quick D-Pad "Switch Vibe" Pill
        Box(
            modifier = Modifier
                .height(42.dp)
                .clip(CircleShape)
                .background(if (isCycleFocused) TvIndigo else TvSurface)
                .border(1.dp, if (isCycleFocused) TvSky else Color(0x33FFFFFF), CircleShape)
                .onFocusChanged { isCycleFocused = it.isFocused }
                .focusable()
                .clickable(onClick = onCyclePreset)
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⟳ Switch Vibe",
                color = TvInk,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.width(24.dp))

        Column(horizontalAlignment = Alignment.End) {
            Row {
                Text("CinePulse ", color = TvInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("TV", color = TvMuted, fontSize = 16.sp)
            }
            Text("TUNED TO YOU", color = TvMuted, fontSize = 9.sp, letterSpacing = 1.sp)
        }
    }
}