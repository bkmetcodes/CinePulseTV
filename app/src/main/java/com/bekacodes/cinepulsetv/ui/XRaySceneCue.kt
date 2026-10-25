package com.bekacodes.cinepulsetv.ui

import androidx.compose.runtime.Immutable

@Immutable
data class XRaySceneCue(
    val timestampSeconds: Int,
    val formattedTime: String,
    val chapterTitle: String,
    val plotInsight: String,
    val castInScene: List<String>,
    val soundtrackTitle: String
)