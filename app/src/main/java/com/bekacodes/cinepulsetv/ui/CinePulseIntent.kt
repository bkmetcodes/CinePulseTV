package com.bekacodes.cinepulsetv.ui

import androidx.compose.runtime.Immutable

@Immutable
sealed interface CinePulseIntent {
    data class SelectNavTab(val tab: TvNavTab) : CinePulseIntent
    data class FocusMovie(val movie: MovieUiModel) : CinePulseIntent
    data class CyclePresetVibePrompt(val prompt: String) : CinePulseIntent
    data class ToggleSearchEditor(val open: Boolean) : CinePulseIntent
    data class UpdateSearchInput(val query: String) : CinePulseIntent
    data object SubmitVibeSearch : CinePulseIntent
    data class ToggleWatchlist(val movieId: String) : CinePulseIntent
    data class StartPlayback(
        val movie: MovieUiModel,
        val openXRayImmediately: Boolean = false
    ) : CinePulseIntent
    data object ClosePlayback : CinePulseIntent
    data class TogglePlayerXRay(val open: Boolean) : CinePulseIntent
}
