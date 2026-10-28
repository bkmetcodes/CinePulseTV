package com.bekacodes.cinepulsetv.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekacodes.cinepulsetv.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CinePulseViewModel @Inject constructor(
    private val repository: CatalogRepository
) : ViewModel() {

    private val selectedTabFlow = MutableStateFlow(TvNavTab.HOME)
    private val vibeQueryFlow =
        MutableStateFlow("Dark sci-fi thrillers under 2 hours with mind-bending endings")
    private val focusedMovieIdFlow = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(CinePulseUiState(allMovies = repository.getCatalogMovies()))
    val uiState: StateFlow<CinePulseUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeWatchlistIds(),
                selectedTabFlow,
                vibeQueryFlow,
                focusedMovieIdFlow
            ) { watchlistIdList, selectedTab, activeQuery, focusedId ->
                val watchlistSet = watchlistIdList.toSet()
                val catalogWithWatchlist = repository.getCatalogMovies().map { movie ->
                    movie.copy(isInWatchlist = movie.id in watchlistSet)
                }

                val rankedByVibe = repository.rankCatalogForVibe(activeQuery, catalogWithWatchlist)

                val displayedMovies = when (selectedTab) {
                    TvNavTab.WATCHLIST -> catalogWithWatchlist.filter { it.id in watchlistSet }
                    TvNavTab.AI_VIBE, TvNavTab.HOME, TvNavTab.SETTINGS -> rankedByVibe
                }

                val activeSpotlight = displayedMovies.find { it.id == focusedId }
                    ?: displayedMovies.firstOrNull()
                    ?: catalogWithWatchlist.first()

                _uiState.value.copy(
                    selectedNavTab = selectedTab,
                    activeVibeQuery = activeQuery,
                    allMovies = catalogWithWatchlist,
                    filteredMovies = displayedMovies,
                    spotlightMovie = activeSpotlight,
                    watchlistIds = watchlistSet
                )
            }.collect { updatedState ->
                _uiState.value = updatedState
            }
        }
    }

    fun onIntent(intent: CinePulseIntent) {
        when (intent) {
            is CinePulseIntent.SelectNavTab -> {
                selectedTabFlow.value = intent.tab
            }

            is CinePulseIntent.FocusMovie -> {
                focusedMovieIdFlow.value = intent.movie.id
            }

            is CinePulseIntent.CyclePresetVibePrompt -> {
                vibeQueryFlow.value = intent.prompt
                focusedMovieIdFlow.value = null
            }

            is CinePulseIntent.ToggleSearchEditor -> {
                _uiState.update {
                    it.copy(
                        isSearchEditorOpen = intent.open,
                        searchInputBuffer = if (intent.open) it.activeVibeQuery else it.searchInputBuffer
                    )
                }
            }

            is CinePulseIntent.UpdateSearchInput -> {
                _uiState.update { it.copy(searchInputBuffer = intent.query) }
            }

            is CinePulseIntent.SubmitVibeSearch -> {
                val query = _uiState.value.searchInputBuffer.trim()
                if (query.isNotEmpty()) {
                    vibeQueryFlow.value = query
                    focusedMovieIdFlow.value = null
                }
                _uiState.update { it.copy(isSearchEditorOpen = false) }
            }

            is CinePulseIntent.ToggleWatchlist -> {
                viewModelScope.launch {
                    repository.toggleWatchlist(intent.movieId)
                }
            }

            is CinePulseIntent.StartPlayback -> {
                _uiState.update {
                    it.copy(
                        activePlaybackMovie = intent.movie,
                        isXRayOpenInPlayer = intent.openXRayImmediately
                    )
                }
            }

            is CinePulseIntent.ClosePlayback -> {
                _uiState.update {
                    it.copy(
                        activePlaybackMovie = null,
                        isXRayOpenInPlayer = false
                    )
                }
            }

            is CinePulseIntent.TogglePlayerXRay -> {
                _uiState.update { it.copy(isXRayOpenInPlayer = intent.open) }
            }
        }
    }
}
