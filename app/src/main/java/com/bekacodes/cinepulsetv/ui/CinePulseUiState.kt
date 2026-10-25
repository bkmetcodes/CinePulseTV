package com.bekacodes.cinepulsetv.ui

import androidx.compose.runtime.Immutable

@Immutable
data class CinePulseUiState(
    val selectedNavTab: TvNavTab = TvNavTab.HOME,
    val activeVibeQuery: String = "Dark sci-fi thrillers under 2 hours with mind-bending endings",
    val presetVibePrompts: List<String> = listOf(
        "Dark sci-fi thrillers under 2 hours with mind-bending endings",
        "High-stakes deep space survival under 110 minutes",
        "Cyberpunk AI mysteries with synthwave soundtracks",
        "All sci-fi originals"
    ),
    val allMovies: List<MovieUiModel> = emptyList(),
    val filteredMovies: List<MovieUiModel> = emptyList(),
    val spotlightMovie: MovieUiModel? = null,
    val watchlistIds: Set<String> = emptySet(),
    val isSearchEditorOpen: Boolean = false,
    val searchInputBuffer: String = "",
    val activePlaybackMovie: MovieUiModel? = null,
    val isXRayOpenInPlayer: Boolean = false
)