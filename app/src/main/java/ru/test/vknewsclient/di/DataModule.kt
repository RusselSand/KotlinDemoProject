package ru.test.vknewsclient.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.test.vknewsclient.domain.entity.PreferencesKeyValueStorage
import ru.test.vknewsclient.data.network.ApiFactory
import ru.test.vknewsclient.data.network.ApiService
import ru.test.vknewsclient.data.repository.NewsFeedRepositoryImpl
import ru.test.vknewsclient.domain.repository.NewsFeedRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository
    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideVkStorage(context: Context): PreferencesKeyValueStorage {
            return PreferencesKeyValueStorage(context)
        }
    }
}