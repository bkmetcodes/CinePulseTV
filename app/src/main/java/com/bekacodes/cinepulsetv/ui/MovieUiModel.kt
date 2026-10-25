package com.bekacodes.cinepulsetv.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MovieUiModel(
    val id: String,
    val title: String,
    val tagLine: String,
    val synopsis: String,
    val runtimeMinutes: Int,
    val formattedRuntime: String,
    val releaseYear: Int,
    val qualityBadge: String,
    val genres: List<String>,
    val moodTags: List<String>,
    val aiMatchPercent: Int,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val streamUrl: String,
    val xRayCues: List<XRaySceneCue>,
    val isInWatchlist: Boolean = false
)