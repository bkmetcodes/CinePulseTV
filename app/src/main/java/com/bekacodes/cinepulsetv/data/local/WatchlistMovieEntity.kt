package com.bekacodes.cinepulsetv.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "tv_watchlist")
data class WatchlistMovieEntity(
    @PrimaryKey val movieId: String,
    val addedAt: Long = System.currentTimeMillis()
)