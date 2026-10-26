package com.bekacodes.cinepulsetv.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(
    entities = [WatchlistMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CinePulseDatabase : RoomDatabase() {
    abstract fun dao(): CinePulseDao

    companion object {
        @Volatile
        private var INSTANCE: CinePulseDatabase? = null

        fun getInstance(context: Context): CinePulseDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CinePulseDatabase::class.java,
                    "cinepulse_tv.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}