package com.dev.profilecreation.di

import com.dev.profilecreation.data.remote.service.FakeNetworkService
import com.dev.profilecreation.domain.service.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService {
        return FakeNetworkService()
    }
}
