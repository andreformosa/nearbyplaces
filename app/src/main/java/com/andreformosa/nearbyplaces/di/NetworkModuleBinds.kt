package com.andreformosa.nearbyplaces.di

import com.andreformosa.nearbyplaces.data.places.RemotePlacesDataSource
import com.andreformosa.nearbyplaces.data.places.RetrofitPlacesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModuleBinds {

    @Binds
    abstract fun bindRemotePlacesDataSource(bind: RetrofitPlacesDataSource): RemotePlacesDataSource
}
