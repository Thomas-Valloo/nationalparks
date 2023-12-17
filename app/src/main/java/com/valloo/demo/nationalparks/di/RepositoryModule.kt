package com.valloo.demo.nationalparks.di

import com.valloo.demo.nationalparks.repo.park.ParkRemoteMediator
import com.valloo.demo.nationalparks.repo.park.ParkRemoteMediatorImpl
import com.valloo.demo.nationalparks.repo.park.ParkRepository
import com.valloo.demo.nationalparks.repo.park.ParkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindParkRepositoryImpl(repository: ParkRepositoryImpl): ParkRepository

    @Binds
    fun bindParkRemoteMediatorImpl(mediator: ParkRemoteMediatorImpl): ParkRemoteMediator

}