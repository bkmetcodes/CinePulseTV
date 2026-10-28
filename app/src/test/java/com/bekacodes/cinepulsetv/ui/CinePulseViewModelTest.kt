package com.bekacodes.cinepulsetv.ui

import com.bekacodes.cinepulsetv.data.catalog.CinePulseCatalog
import com.bekacodes.cinepulsetv.data.local.CinePulseDao
import com.bekacodes.cinepulsetv.data.local.WatchlistMovieEntity
import com.bekacodes.cinepulsetv.data.repository.CatalogRepository
import com.bekacodes.cinepulsetv.domain.ai.VibeSearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class FakeCinePulseDao : CinePulseDao {
    private val watchlistFlow = MutableStateFlow<List<String>>(emptyList())

    override fun observeWatchlistIds(): Flow<List<String>> = watchlistFlow

    override suspend fun isWatchlisted(movieId: String): Boolean {
        return watchlistFlow.value.contains(movieId)
    }

    override suspend fun addToWatchlist(entity: WatchlistMovieEntity) {
        if (!watchlistFlow.value.contains(entity.movieId)) {
            watchlistFlow.value += entity.movieId
        }
    }

    override suspend fun removeFromWatchlist(movieId: String) {
        watchlistFlow.value -= movieId
    }
}

class FakeCatalogRepository(
    private val dao: CinePulseDao,
    private val vibeEngine: VibeSearchEngine = VibeSearchEngine()
) : CatalogRepository {

    override fun observeWatchlistIds(): Flow<List<String>> = dao.observeWatchlistIds()

    override fun getCatalogMovies(): List<MovieUiModel> = CinePulseCatalog.movies

    override fun rankCatalogForVibe(
        query: String,
        movies: List<MovieUiModel>
    ): List<MovieUiModel> {
        return vibeEngine.rankCatalogForVibe(query, movies)
    }

    override suspend fun toggleWatchlist(movieId: String): Boolean {
        return dao.toggleWatchlist(movieId)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CinePulseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeDao: FakeCinePulseDao
    private lateinit var vibeEngine: VibeSearchEngine
    private lateinit var fakeRepository: FakeCatalogRepository
    private lateinit var viewModel: CinePulseViewModel

    @Before
    fun setUp() {
        fakeDao = FakeCinePulseDao()
        vibeEngine = VibeSearchEngine()
        fakeRepository = FakeCatalogRepository(fakeDao, vibeEngine)
        viewModel = CinePulseViewModel(fakeRepository)
    }

    @Test
    fun initialization_populatesUiStateWithRankedCatalogAndDefaultTab() = runTest {
        val state = viewModel.uiState.value

        assertEquals(TvNavTab.HOME, state.selectedNavTab)
        assertEquals(
            "Dark sci-fi thrillers under 2 hours with mind-bending endings",
            state.activeVibeQuery
        )
        assertEquals(CinePulseCatalog.movies.size, state.allMovies.size)
        assertTrue(state.filteredMovies.isNotEmpty())
        assertNotNull(state.spotlightMovie)
        assertEquals(state.filteredMovies.first().id, state.spotlightMovie?.id)
        assertTrue(state.watchlistIds.isEmpty())
    }

    @Test
    fun selectNavTab_updatesSelectedTabInUiState() = runTest {
        viewModel.onIntent(CinePulseIntent.SelectNavTab(TvNavTab.SETTINGS))

        assertEquals(TvNavTab.SETTINGS, viewModel.uiState.value.selectedNavTab)
    }

    @Test
    fun selectNavTab_watchlistTab_filtersMoviesToWatchlistOnly() = runTest {
        val targetMovieId = "solaris"
        viewModel.onIntent(CinePulseIntent.ToggleWatchlist(targetMovieId))

        viewModel.onIntent(CinePulseIntent.SelectNavTab(TvNavTab.WATCHLIST))

        val state = viewModel.uiState.value
        assertEquals(TvNavTab.WATCHLIST, state.selectedNavTab)
        assertEquals(1, state.filteredMovies.size)
        assertEquals(targetMovieId, state.filteredMovies.first().id)
        assertEquals(setOf(targetMovieId), state.watchlistIds)
    }

    @Test
    fun focusMovie_updatesSpotlightMovie() = runTest {
        val targetMovie = CinePulseCatalog.movies[2] // "orbital"

        viewModel.onIntent(CinePulseIntent.FocusMovie(targetMovie))

        assertEquals(targetMovie.id, viewModel.uiState.value.spotlightMovie?.id)
    }

    @Test
    fun cyclePresetVibePrompt_updatesActiveVibeQueryAndResetsFocusedMovie() = runTest {
        val movieToFocus = CinePulseCatalog.movies.last()
        viewModel.onIntent(CinePulseIntent.FocusMovie(movieToFocus))

        val newPrompt = "Cyberpunk AI mysteries with synthwave soundtracks"
        viewModel.onIntent(CinePulseIntent.CyclePresetVibePrompt(newPrompt))

        val state = viewModel.uiState.value
        assertEquals(newPrompt, state.activeVibeQuery)
        assertEquals(state.filteredMovies.first().id, state.spotlightMovie?.id)
    }

    @Test
    fun toggleSearchEditor_opensAndClosesSearchEditorAndSetsInputBuffer() = runTest {
        viewModel.onIntent(CinePulseIntent.ToggleSearchEditor(open = true))

        var state = viewModel.uiState.value
        assertTrue(state.isSearchEditorOpen)
        assertEquals(state.activeVibeQuery, state.searchInputBuffer)

        viewModel.onIntent(CinePulseIntent.ToggleSearchEditor(open = false))

        state = viewModel.uiState.value
        assertFalse(state.isSearchEditorOpen)
    }

    @Test
    fun updateSearchInput_updatesSearchInputBuffer() = runTest {
        val searchBuffer = "High-stakes deep space survival"

        viewModel.onIntent(CinePulseIntent.UpdateSearchInput(searchBuffer))

        assertEquals(searchBuffer, viewModel.uiState.value.searchInputBuffer)
    }

    @Test
    fun submitVibeSearch_withValidQuery_updatesVibeQueryAndClosesEditor() = runTest {
        viewModel.onIntent(CinePulseIntent.ToggleSearchEditor(open = true))
        viewModel.onIntent(CinePulseIntent.UpdateSearchInput("Cyberpunk AI mysteries"))
        viewModel.onIntent(CinePulseIntent.SubmitVibeSearch)

        val state = viewModel.uiState.value
        assertEquals("Cyberpunk AI mysteries", state.activeVibeQuery)
        assertFalse(state.isSearchEditorOpen)
    }

    @Test
    fun submitVibeSearch_withBlankQuery_doesNotUpdateVibeQueryAndClosesEditor() = runTest {
        val initialQuery = viewModel.uiState.value.activeVibeQuery

        viewModel.onIntent(CinePulseIntent.ToggleSearchEditor(open = true))
        viewModel.onIntent(CinePulseIntent.UpdateSearchInput("   "))
        viewModel.onIntent(CinePulseIntent.SubmitVibeSearch)

        val state = viewModel.uiState.value
        assertEquals(initialQuery, state.activeVibeQuery)
        assertFalse(state.isSearchEditorOpen)
    }

    @Test
    fun toggleWatchlist_addsAndRemovesMovieFromWatchlist() = runTest {
        val movieId = "solaris"

        viewModel.onIntent(CinePulseIntent.ToggleWatchlist(movieId))

        var state = viewModel.uiState.value
        assertTrue(state.watchlistIds.contains(movieId))
        val addedMovie = state.allMovies.find { it.id == movieId }
        assertNotNull(addedMovie)
        assertTrue(addedMovie!!.isInWatchlist)

        viewModel.onIntent(CinePulseIntent.ToggleWatchlist(movieId))

        state = viewModel.uiState.value
        assertFalse(state.watchlistIds.contains(movieId))
        val removedMovie = state.allMovies.find { it.id == movieId }
        assertNotNull(removedMovie)
        assertFalse(removedMovie!!.isInWatchlist)
    }

    @Test
    fun startPlayback_updatesActivePlaybackMovieAndXRayState() = runTest {
        val movie = CinePulseCatalog.movies.first()

        viewModel.onIntent(CinePulseIntent.StartPlayback(movie = movie, openXRayImmediately = true))

        val state = viewModel.uiState.value
        assertEquals(movie, state.activePlaybackMovie)
        assertTrue(state.isXRayOpenInPlayer)
    }

    @Test
    fun closePlayback_clearsActivePlaybackMovieAndResetsXRayState() = runTest {
        val movie = CinePulseCatalog.movies.first()
        viewModel.onIntent(CinePulseIntent.StartPlayback(movie = movie, openXRayImmediately = true))

        viewModel.onIntent(CinePulseIntent.ClosePlayback)

        val state = viewModel.uiState.value
        assertNull(state.activePlaybackMovie)
        assertFalse(state.isXRayOpenInPlayer)
    }

    @Test
    fun togglePlayerXRay_updatesIsXRayOpenInPlayer() = runTest {
        viewModel.onIntent(CinePulseIntent.TogglePlayerXRay(open = true))
        assertTrue(viewModel.uiState.value.isXRayOpenInPlayer)

        viewModel.onIntent(CinePulseIntent.TogglePlayerXRay(open = false))
        assertFalse(viewModel.uiState.value.isXRayOpenInPlayer)
    }
}