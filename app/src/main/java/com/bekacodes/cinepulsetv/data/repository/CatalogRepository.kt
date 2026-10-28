package com.bekacodes.cinepulsetv.data.repository

import com.bekacodes.cinepulsetv.ui.MovieUiModel
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    fun observeWatchlistIds(): Flow<List<String>>
    fun getCatalogMovies(): List<MovieUiModel>
    fun rankCatalogForVibe(query: String, movies: List<MovieUiModel>): List<MovieUiModel>
    suspend fun toggleWatchlist(movieId: String): Boolean
}
