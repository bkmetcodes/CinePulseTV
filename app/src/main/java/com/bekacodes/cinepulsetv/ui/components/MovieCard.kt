package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.bekacodes.cinepulsetv.ui.MovieUiModel
import com.bekacodes.cinepulsetv.ui.theme.TvBg
import com.bekacodes.cinepulsetv.ui.theme.TvCard
import com.bekacodes.cinepulsetv.ui.theme.TvEmerald
import com.bekacodes.cinepulsetv.ui.theme.TvIndigo
import com.bekacodes.cinepulsetv.ui.theme.TvInk
import com.bekacodes.cinepulsetv.ui.theme.TvSky

@Composable
fun MovieCard(
    movie: MovieUiModel,
    isTopMatch: Boolean,
    isSelectedInSpotlight: Boolean,
    isInWatchlist: Boolean,
    onFocused: () -> Unit,
    onClicked: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.08f else 1.0f,
        animationSpec = tween(durationMillis = 180),
        label = "CardFocusScale"
    )
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .width(184.dp)
            .scale(scale)
            .onFocusChanged { state ->
                isFocused = state.isFocused
                if (state.isFocused) onFocused()
            }
            .focusable()
            .clickable(onClick = onClicked),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 184.dp, height = 104.dp)
                .shadow(
                    elevation = if (isFocused) 20.dp else 0.dp,
                    shape = shape,
                    ambientColor = TvIndigo,
                    spotColor = TvIndigo
                )
                .clip(shape)
                .background(TvCard)
                .border(
                    BorderStroke(
                        width = if (isFocused) 2.5.dp else if (isSelectedInSpotlight) 1.5.dp else 1.dp,
                        color = when {
                            isFocused -> TvSky
                            isSelectedInSpotlight -> TvIndigo
                            else -> Color(0x18FFFFFF)
                        }
                    ),
                    shape
                )
        ) {
            // Procedural Sci-Fi Card Artwork
            SciFiArtworkCanvas(
                primaryColor = movie.primaryAccent,
                secondaryColor = movie.secondaryAccent,
                seed = movie.id.hashCode(),
                modifier = Modifier.fillMaxSize()
            )

            if (isTopMatch || isFocused) {
                Text(
                    text = if (isTopMatch) "✦ TOP MATCH" else "▶ PLAY",
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(TvBg.copy(alpha = 0.82f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    color = if (isFocused) TvSky else TvInk,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isInWatchlist) {
                Text(
                    text = "★",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    color = TvEmerald,
                    fontSize = 12.sp
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = movie.title,
                color = if (isFocused) TvSky else TvInk,
                fontSize = 14.sp,
                fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${movie.aiMatchPercent}% Match · ${movie.formattedRuntime}",
                color = TvEmerald,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}