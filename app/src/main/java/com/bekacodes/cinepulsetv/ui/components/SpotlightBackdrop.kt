package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bekacodes.cinepulsetv.ui.MovieUiModel
import com.bekacodes.cinepulsetv.ui.theme.TvBg

@Composable
fun SpotlightBackdrop(movie: MovieUiModel) {
    AnimatedContent(
        targetState = movie,
        transitionSpec = { fadeIn(tween(450)) togetherWith fadeOut(tween(450)) },
        label = "SpotlightBackdropCrossfade"
    ) { targetMovie ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        ) {
            // Procedural Sci-Fi Widescreen Artwork on the right 85%
            SciFiArtworkCanvas(
                primaryColor = targetMovie.primaryAccent,
                secondaryColor = targetMovie.secondaryAccent,
                seed = targetMovie.id.hashCode(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.86f)
                    .fillMaxHeight()
            )

            // Exact Figma Horizontal Vignette Gradient
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            0f to TvBg,
                            0.22f to TvBg.copy(alpha = 0.96f),
                            0.48f to TvBg.copy(alpha = 0.68f),
                            0.75f to TvBg.copy(alpha = 0.12f),
                            1f to Color.Transparent
                        )
                    )
            )

            // Exact Figma Vertical Vignette Gradient
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to TvBg.copy(alpha = 0.30f),
                            0.36f to Color.Transparent,
                            0.72f to TvBg.copy(alpha = 0.35f),
                            1f to TvBg
                        )
                    )
            )
        }
    }
}