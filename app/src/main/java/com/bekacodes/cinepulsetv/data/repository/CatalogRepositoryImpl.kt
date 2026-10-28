package com.bekacodes.cinepulsetv.data.repository

import com.bekacodes.cinepulsetv.data.catalog.CinePulseCatalog
import com.bekacodes.cinepulsetv.data.local.CinePulseDao
import com.bekacodes.cinepulsetv.domain.ai.VibeSearchEngine
import com.bekacodes.cinepulsetv.ui.MovieUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepositoryImpl @Inject constructor(
    private val dao: CinePulseDao,
    private val vibeEngine: VibeSearchEngine
) : CatalogRepository {

    override fun observeWatchlistIds(): Flow<List<String>> {
        return dao.observeWatchlistIds()
    }

    override fun getCatalogMovies(): List<MovieUiModel> {
        return CinePulseCatalog.movies
    }

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
