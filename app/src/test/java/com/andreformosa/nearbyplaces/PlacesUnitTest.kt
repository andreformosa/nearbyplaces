package com.andreformosa.nearbyplaces

import com.andreformosa.nearbyplaces.common.JsonParser
import com.andreformosa.nearbyplaces.data.api.PlacesService
import com.andreformosa.nearbyplaces.data.api.VenueRecommendationsQueryBuilder
import com.andreformosa.nearbyplaces.di.NetworkModule
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PlacesUnitTest {

    private lateinit var placesService: PlacesService

    @Before
    fun setupService() {
        placesService = NetworkModule.providePlacesService(
            okHttpClient = OkHttpClient().newBuilder().build(),
            retrofit = NetworkModule.provideRetrofitBuilder(JsonParser())
        )
    }

    @Test
    fun testResponseCode() {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(52.376510, 4.905890)
            .build()

        runBlocking {
            placesService.getVenueRecommendations(query)
                .suspendOnSuccess {
                    assertNotNull("Response is null.", response)
                    assertEquals("Response code", 200, response.code())
                }
                .suspendOnError {
                    assertNull("Received an error: ${errorBody?.string()}", errorBody)
                }
        }
    }
}
