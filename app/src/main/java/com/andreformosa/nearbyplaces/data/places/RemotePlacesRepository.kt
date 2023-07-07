package com.andreformosa.nearbyplaces.data.places

import com.skydoves.sandwich.ApiResponse
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemotePlacesRepository @Inject constructor(
    private val remotePlacesDataSource: RemotePlacesDataSource
) : PlacesRepository {

    override suspend fun getNearbyVenues(latitude: Double, longitude: Double): PlacesResult {
        return when (val response = remotePlacesDataSource.getNearbyVenues(latitude, longitude)) {
            is ApiResponse.Success -> PlacesResult.Success(response.data.results)

            is ApiResponse.Failure.Error -> {
                Timber.e(response.statusCode.toString())
                PlacesResult.GenericError
            }

            is ApiResponse.Failure.Exception -> {
                Timber.e(
                    response.exception,
                    response.exception.message ?: "Exception when getting nearby venues",
                )
                PlacesResult.GenericError
            }
        }
    }
}
