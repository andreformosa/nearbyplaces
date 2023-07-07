package com.andreformosa.nearbyplaces.di

import com.andreformosa.nearbyplaces.data.places.PlacesRepository
import com.andreformosa.nearbyplaces.data.places.RemotePlacesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Binds
    abstract fun bindPlacesRepository(bind: RemotePlacesRepository): PlacesRepository
}
