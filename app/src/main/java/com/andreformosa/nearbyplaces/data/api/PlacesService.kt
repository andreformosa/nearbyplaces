package com.andreformosa.nearbyplaces.data.api

import com.andreformosa.nearbyplaces.BuildConfig
import com.andreformosa.nearbyplaces.data.api.model.PlacesResponseWrapper
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface PlacesService {

    @Headers("Authorization: ${BuildConfig.FOURSQUARE_API_KEY}")
    @GET("places/nearby")
    suspend fun getVenueRecommendations(
        @QueryMap query: Map<String, String>
    ): ApiResponse<PlacesResponseWrapper>
}
