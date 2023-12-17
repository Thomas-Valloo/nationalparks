package com.valloo.demo.nationalparks.di

import android.content.Context
import androidx.work.WorkManager
import com.valloo.demo.nationalparks.infra.PreferencesStore
import com.valloo.demo.nationalparks.infra.PreferencesStoreImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface AppModule {

    @Binds fun providePreferencesStore(preferencesStore: PreferencesStoreImpl): PreferencesStore

    companion object {
        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext appContext: Context): WorkManager =
            WorkManager.getInstance(appContext)
    }
}
