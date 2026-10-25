package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.bekacodes.cinepulsetv.ui.MovieUiModel
import com.bekacodes.cinepulsetv.ui.theme.TvBg
import com.bekacodes.cinepulsetv.ui.theme.TvBody
import com.bekacodes.cinepulsetv.ui.theme.TvEmerald
import com.bekacodes.cinepulsetv.ui.theme.TvIndigo
import com.bekacodes.cinepulsetv.ui.theme.TvInk
import com.bekacodes.cinepulsetv.ui.theme.TvMuted
import com.bekacodes.cinepulsetv.ui.theme.TvSky
import com.bekacodes.cinepulsetv.ui.theme.TvSurface

@Composable
fun SpotlightDetails(
    movie: MovieUiModel,
    isWatchlisted: Boolean,
    onPlay: () -> Unit,
    onOpenXRay: () -> Unit,
    onToggleWatchlist: () -> Unit
) {
    var playFocused by remember { mutableStateOf(false) }
    var xRayFocused by remember { mutableStateOf(false) }
    var watchlistFocused by remember { mutableStateOf(false) }
    val playFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        runCatching { playFocusRequester.requestFocus() }
    }

    Column(
        modifier = Modifier.widthIn(max = 540.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "CINEPULSE ORIGINAL  /  TONIGHT’S SPOTLIGHT",
            color = TvMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp
        )

        // Emerald AI Vibe Match Pill
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(TvEmerald.copy(alpha = 0.14f))
                .border(BorderStroke(1.dp, TvEmerald.copy(alpha = 0.4f)), CircleShape)
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("✦", color = TvEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "${movie.aiMatchPercent}% AI Vibe Match",
                color = TvEmerald,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = movie.title.uppercase(),
            color = TvInk,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${movie.qualityBadge} · Dolby Atmos · ${movie.formattedRuntime} · ${
                movie.genres.joinToString(
                    ", "
                )
            }",
            color = TvMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = movie.synopsis,
            color = TvBody,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Action CTAs with Real D-Pad Focus Rings
        Row(
            modifier = Modifier.padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val playScale by animateFloatAsState(
                if (playFocused) 1.06f else 1f,
                label = "playScale"
            )
            Row(
                modifier = Modifier
                    .scale(playScale)
                    .height(44.dp)
                    .shadow(
                        if (playFocused) 16.dp else 4.dp,
                        CircleShape,
                        ambientColor = TvSky,
                        spotColor = TvSky
                    )
                    .clip(CircleShape)
                    .background(if (playFocused) TvInk else TvInk.copy(alpha = 0.9f))
                    .border(
                        BorderStroke(
                            if (playFocused) 2.5.dp else 0.dp,
                            if (playFocused) TvSky else Color.Transparent
                        ),
                        CircleShape
                    )
                    .focusRequester(playFocusRequester)
                    .onFocusChanged { playFocused = it.isFocused }
                    .focusable()
                    .clickable(onClick = onPlay)
                    .padding(horizontal = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("▶", color = TvBg, fontSize = 14.sp)
                Text("Play Movie", color = TvBg, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            val xRayScale by animateFloatAsState(
                if (xRayFocused) 1.06f else 1f,
                label = "xRayScale"
            )
            Row(
                modifier = Modifier
                    .scale(xRayScale)
                    .height(44.dp)
                    .clip(CircleShape)
                    .background(if (xRayFocused) TvIndigo.copy(alpha = 0.35f) else TvSurface)
                    .border(
                        BorderStroke(
                            if (xRayFocused) 2.dp else 1.dp,
                            if (xRayFocused) TvSky else Color(0x28FFFFFF)
                        ),
                        CircleShape
                    )
                    .onFocusChanged { xRayFocused = it.isFocused }
                    .focusable()
                    .clickable(onClick = onOpenXRay)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("✦", color = TvSky, fontSize = 15.sp)
                Text(
                    "AI Scene X-Ray",
                    color = TvInk,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            val watchScale by animateFloatAsState(
                if (watchlistFocused) 1.06f else 1f,
                label = "watchScale"
            )
            Row(
                modifier = Modifier
                    .scale(watchScale)
                    .height(44.dp)
                    .clip(CircleShape)
                    .background(TvSurface)
                    .border(
                        BorderStroke(
                            if (watchlistFocused) 2.dp else 1.dp,
                            if (watchlistFocused) TvSky else Color(0x28FFFFFF)
                        ),
                        CircleShape
                    )
                    .onFocusChanged { watchlistFocused = it.isFocused }
                    .focusable()
                    .clickable(onClick = onToggleWatchlist)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (isWatchlisted) "★ Saved" else "+ Watchlist",
                    color = if (isWatchlisted) TvEmerald else TvInk,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}