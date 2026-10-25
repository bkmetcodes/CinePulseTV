package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.bekacodes.cinepulsetv.ui.theme.TvBg

/**
 * Draws atmospheric Sci-Fi cinema artwork (glowing nebula, planetary horizon, orbital rings, starfield)
 * so the app compiles cleanly without missing PNG resources.
 */
@Composable
fun SciFiArtworkCanvas(
    primaryColor: Color,
    secondaryColor: Color,
    seed: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.background(TvBg)) {
        val w = size.width
        val h = size.height

        // Deep space nebula gradient
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.55f),
                    secondaryColor.copy(alpha = 0.25f),
                    TvBg
                ),
                center = Offset(w * 0.65f, h * 0.42f),
                radius = w * 0.65f
            )
        )

        // Glowing planet / eclipse orb
        val orbCenter = Offset(w * (0.58f + (seed % 15) * 0.01f), h * 0.48f)
        val orbRadius = h * 0.34f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(secondaryColor.copy(alpha = 0.7f), Color.Transparent),
                center = orbCenter,
                radius = orbRadius * 1.4f
            ),
            radius = orbRadius * 1.4f,
            center = orbCenter
        )

        drawCircle(
            color = Color(0xFF060911),
            radius = orbRadius,
            center = orbCenter
        )

        // Rim light arc
        drawCircle(
            color = secondaryColor.copy(alpha = 0.85f),
            radius = orbRadius,
            center = orbCenter,
            style = Stroke(width = 2.dp.toPx())
        )

        // Orbital telemetry rings
        drawCircle(
            color = primaryColor.copy(alpha = 0.32f),
            radius = orbRadius * 1.55f,
            center = orbCenter,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}