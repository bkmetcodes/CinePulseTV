package com.bekacodes.cinepulsetv.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bekacodes.cinepulsetv.ui.components.MoodRow
import com.bekacodes.cinepulsetv.ui.components.NavigationRail
import com.bekacodes.cinepulsetv.ui.components.SearchBar
import com.bekacodes.cinepulsetv.ui.components.SpotlightBackdrop
import com.bekacodes.cinepulsetv.ui.components.SpotlightDetails
import com.bekacodes.cinepulsetv.ui.components.VibeQueryDialog
import com.bekacodes.cinepulsetv.ui.theme.TvBg

@Composable
fun CinePulseTvScreen(
    uiState: CinePulseUiState,
    onIntent: (CinePulseIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeHero = uiState.spotlightMovie ?: uiState.filteredMovies.firstOrNull()

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(TvBg)
    ) {
        // 1. Left D-Pad Navigation Rail
        NavigationRail(
            selectedTab = uiState.selectedNavTab,
            watchlistCount = uiState.watchlistIds.size,
            onSelectTab = { onIntent(CinePulseIntent.SelectNavTab(it)) }
        )

        // 2. Main 10-Foot Stage
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // Animated Full-Bleed Widescreen Backdrop
            if (activeHero != null) {
                SpotlightBackdrop(movie = activeHero)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 36.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Floating AI Vibe Search Bar
                SearchBar(
                    activeQuery = uiState.activeVibeQuery,
                    resultCount = uiState.filteredMovies.size,
                    onClickSearch = { onIntent(CinePulseIntent.ToggleSearchEditor(true)) },
                    onCyclePreset = {
                        val list = uiState.presetVibePrompts
                        val nextIdx = (list.indexOf(uiState.activeVibeQuery) + 1) % list.size
                        onIntent(CinePulseIntent.CyclePresetVibePrompt(list[nextIdx]))
                    }
                )

                // Center-Left Hero Spotlight Details
                if (activeHero != null) {
                    SpotlightDetails(
                        movie = activeHero,
                        isWatchlisted = activeHero.id in uiState.watchlistIds,
                        isPlayerOpen = uiState.activePlaybackMovie != null,
                        onPlay = {
                            onIntent(
                                CinePulseIntent.StartPlayback(
                                    activeHero,
                                    openXRayImmediately = false
                                )
                            )
                        },
                        onOpenXRay = {
                            onIntent(
                                CinePulseIntent.StartPlayback(
                                    activeHero,
                                    openXRayImmediately = true
                                )
                            )
                        },
                        onToggleWatchlist = { onIntent(CinePulseIntent.ToggleWatchlist(activeHero.id)) }
                    )
                }

                // Bottom D-Pad Horizontal Poster Carousel
                MoodRow(
                    sectionTitle = when (uiState.selectedNavTab) {
                        TvNavTab.WATCHLIST -> "★ Your Saved Watchlist"
                        TvNavTab.AI_VIBE -> "✦ Ranked by AI Vibe Engine"
                        else -> "AI Curated for Your Mood"
                    },
                    movies = uiState.filteredMovies,
                    spotlightMovieId = activeHero?.id,
                    watchlistIds = uiState.watchlistIds,
                    onMovieFocused = { onIntent(CinePulseIntent.FocusMovie(it)) },
                    onMovieClicked = {
                        onIntent(
                            CinePulseIntent.StartPlayback(
                                it,
                                openXRayImmediately = false
                            )
                        )
                    }
                )
            }
        }
    }

    // Optional Custom AI Vibe Query Dialog
    if (uiState.isSearchEditorOpen) {
        VibeQueryDialog(
            initialQuery = uiState.searchInputBuffer,
            presetPrompts = uiState.presetVibePrompts,
            onQueryChanged = { onIntent(CinePulseIntent.UpdateSearchInput(it)) },
            onSelectPreset = { preset ->
                onIntent(CinePulseIntent.CyclePresetVibePrompt(preset))
                onIntent(CinePulseIntent.ToggleSearchEditor(false))
            },
            onSubmit = { onIntent(CinePulseIntent.SubmitVibeSearch) },
            onDismiss = { onIntent(CinePulseIntent.ToggleSearchEditor(false)) }
        )
    }

    // Full-Screen 10-Foot Video Player + AI Scene X-Ray Overlay
    AnimatedVisibility(
        visible = uiState.activePlaybackMovie != null,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(250))
    ) {
        uiState.activePlaybackMovie?.let { playingMovie ->
            CinePulsePlayerScreen(
                movie = playingMovie,
                isXRayOpen = uiState.isXRayOpenInPlayer,
                onToggleXRay = { open -> onIntent(CinePulseIntent.TogglePlayerXRay(open)) },
                onClosePlayer = { onIntent(CinePulseIntent.ClosePlayback) }
            )
        }
    }
}

@Preview(
    name = "Android TV 1080p (960x540dp)",
    widthDp = 960,
    heightDp = 540,
    showBackground = true
)
@Composable
private fun CinePulseTvScreenPreview() {
    val sampleMovies = listOf(
        MovieUiModel(
            id = "solaris",
            title = "Solaris Protocol",
            tagLine = "CINEPULSE ORIGINAL",
            synopsis = "A signal from a vanished world. A memory that isn't hers. An astronaut discovers the universe is keeping a secret.",
            runtimeMinutes = 112,
            formattedRuntime = "1h 52m",
            releaseYear = 2026,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Thriller"),
            moodTags = listOf("dark", "mind-bending", "space", "thriller"),
            aiMatchPercent = 98,
            primaryAccent = Color(0xFF6366F1),
            secondaryAccent = Color(0xFF38BDF8),
            streamUrl = "",
            xRayCues = emptyList()
        ),
        MovieUiModel(
            id = "chronos",
            title = "Chronos Echo",
            tagLine = "TIME LOOP THRILLER",
            synopsis = "A quantum cryptographer intercepts a distress call transmitted by her own future self 48 minutes before orbital collapse.",
            runtimeMinutes = 108,
            formattedRuntime = "1h 48m",
            releaseYear = 2026,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Mystery"),
            moodTags = listOf("dark", "mind-bending", "time", "thriller"),
            aiMatchPercent = 98,
            primaryAccent = Color(0xFF8B5CF6),
            secondaryAccent = Color(0xFF10B981),
            streamUrl = "",
            xRayCues = emptyList()
        ),
        MovieUiModel(
            id = "orbital",
            title = "Orbital Zero",
            tagLine = "DEEP SPACE SURVIVAL",
            synopsis = "Cut off from Earth telemetry, a three-person salvage crew boards a derelict fusion ark drifting past Titan.",
            runtimeMinutes = 99,
            formattedRuntime = "1h 39m",
            releaseYear = 2025,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Survival"),
            moodTags = listOf("dark", "space", "survival"),
            aiMatchPercent = 95,
            primaryAccent = Color(0xFF0EA5E9),
            secondaryAccent = Color(0xFF6366F1),
            streamUrl = "",
            xRayCues = emptyList()
        ),
        MovieUiModel(
            id = "neural",
            title = "Neural Drift",
            tagLine = "CYBERPUNK NOIR",
            synopsis = "In Neo-Veracruz, a synthetic memory architect uncovers a hidden partition inside a reigning senator's neural backup.",
            runtimeMinutes = 116,
            formattedRuntime = "1h 56m",
            releaseYear = 2026,
            qualityBadge = "Dolby Vision",
            genres = listOf("Sci-Fi", "Cyberpunk"),
            moodTags = listOf("cyberpunk", "mind-bending", "synthwave", "dark"),
            aiMatchPercent = 92,
            primaryAccent = Color(0xFFEC4899),
            secondaryAccent = Color(0xFF6366F1),
            streamUrl = "",
            xRayCues = emptyList()
        ),
        MovieUiModel(
            id = "horizon",
            title = "Event Horizon X",
            tagLine = "SINGULARITY EXPEDITION",
            synopsis = "The first faster-than-light probe returns after seven years in deep sector space—bringing back an impossible passenger.",
            runtimeMinutes = 104,
            formattedRuntime = "1h 44m",
            releaseYear = 2025,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Horror"),
            moodTags = listOf("dark", "space", "thriller", "survival"),
            aiMatchPercent = 89,
            primaryAccent = Color(0xFFF43F5E),
            secondaryAccent = Color(0xFF8B5CF6),
            streamUrl = "",
            xRayCues = emptyList()
        )
    )

    CinePulseTvScreen(
        uiState = CinePulseUiState(
            allMovies = sampleMovies,
            filteredMovies = sampleMovies,
            spotlightMovie = sampleMovies.first()
        ),
        onIntent = {}
    )
}