package com.andreformosa.nearbyplaces.data.places

import com.andreformosa.nearbyplaces.data.api.PlacesService
import com.andreformosa.nearbyplaces.data.api.VenueRecommendationsQueryBuilder
import com.andreformosa.nearbyplaces.data.api.model.PlacesResponseWrapper
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class RetrofitPlacesDataSource @Inject constructor(
    private val service: PlacesService
) : RemotePlacesDataSource {

    override suspend fun getNearbyVenues(
        latitude: Double,
        longitude: Double
    ): ApiResponse<PlacesResponseWrapper> {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude, longitude)
            .build()
        return service.getVenueRecommendations(query)
    }
}
