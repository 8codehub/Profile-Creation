package com.dev.profilecreation.di

import com.dev.profilecreation.data.repository.RegistrationRepository
import com.dev.profilecreation.domain.repository.IRegistrationRepository
import com.dev.profilecreation.domain.service.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRegistrationRepository(networkService: NetworkService): IRegistrationRepository {
        return RegistrationRepository(networkService)
    }
}
