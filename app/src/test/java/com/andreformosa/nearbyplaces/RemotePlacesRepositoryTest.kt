package com.andreformosa.nearbyplaces

import com.andreformosa.nearbyplaces.data.api.model.PlacesResponseWrapper
import com.andreformosa.nearbyplaces.data.places.PlacesResult
import com.andreformosa.nearbyplaces.data.places.RemotePlacesDataSource
import com.andreformosa.nearbyplaces.data.places.RemotePlacesRepository
import com.andreformosa.nearbyplaces.factory.createPlace
import com.andreformosa.nearbyplaces.utils.MainDispatcherRule
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import retrofit2.Response

class RemotePlacesRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val remotePlacesDataSource = mock<RemotePlacesDataSource>()

    private lateinit var repository: RemotePlacesRepository

    @Before
    fun setUp() {
        repository = RemotePlacesRepository(remotePlacesDataSource)
    }

    @Test
    fun `get nearby venues - success`() = runTest {
        // Arrange
        val expectedPlaces = listOf(createPlace())
        val expectedResponse = PlacesResult.Success(expectedPlaces)
        whenever(
            remotePlacesDataSource.getNearbyVenues(any(), any())
        ) doReturn ApiResponse.Success(Response.success(PlacesResponseWrapper(expectedPlaces)))

        // Act
        val actualResponse = repository.getNearbyVenues(1.0, 2.0)

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `get nearby restaurants - error`() = runTest {
        // Arrange
        val expectedResponse = PlacesResult.GenericError
        whenever(
            remotePlacesDataSource.getNearbyVenues(any(), any())
        ) doReturn ApiResponse.Failure.Error(Response.error(400, "Error message".toResponseBody()))

        // Act
        val actualResponse = repository.getNearbyVenues(1.0, 2.0)

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }
}
