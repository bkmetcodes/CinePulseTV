package com.bekacodes.cinepulsetv.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CinePulseDao {
    @Query("SELECT movieId FROM tv_watchlist ORDER BY addedAt DESC")
    fun observeWatchlistIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM tv_watchlist WHERE movieId = :movieId)")
    suspend fun isWatchlisted(movieId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(entity: WatchlistMovieEntity)

    @Query("DELETE FROM tv_watchlist WHERE movieId = :movieId")
    suspend fun removeFromWatchlist(movieId: String)

    @Transaction
    suspend fun toggleWatchlist(movieId: String): Boolean {
        return if (isWatchlisted(movieId)) {
            removeFromWatchlist(movieId)
            false
        } else {
            addToWatchlist(WatchlistMovieEntity(movieId = movieId))
            true
        }
    }
}