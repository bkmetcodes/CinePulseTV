package com.bekacodes.cinepulsetv.di

import android.content.Context
import com.bekacodes.cinepulsetv.data.local.CinePulseDao
import com.bekacodes.cinepulsetv.data.local.CinePulseDatabase
import com.bekacodes.cinepulsetv.domain.ai.VibeSearchEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TvAppModule {

    @Provides
    @Singleton
    fun provideCinePulseDatabase(
        @ApplicationContext context: Context
    ): CinePulseDatabase {
        return CinePulseDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCinePulseDao(
        database: CinePulseDatabase
    ): CinePulseDao {
        return database.dao()
    }

    @Provides
    @Singleton
    fun provideVibeSearchEngine(): VibeSearchEngine {
        return VibeSearchEngine()
    }
}
