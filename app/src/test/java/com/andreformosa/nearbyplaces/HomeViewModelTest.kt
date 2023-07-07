package com.andreformosa.nearbyplaces

import app.cash.turbine.test
import com.andreformosa.nearbyplaces.data.places.PlacesRepository
import com.andreformosa.nearbyplaces.data.places.PlacesResult
import com.andreformosa.nearbyplaces.factory.createPlace
import com.andreformosa.nearbyplaces.feature.home.HomeUiState
import com.andreformosa.nearbyplaces.feature.home.HomeViewModel
import com.andreformosa.nearbyplaces.feature.home.Location
import com.andreformosa.nearbyplaces.feature.home.MapState
import com.andreformosa.nearbyplaces.feature.home.VenuesState
import com.andreformosa.nearbyplaces.utils.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val placesRepository = mock<PlacesRepository>()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            placesRepository = placesRepository
        )
    }

    @Test
    fun `initial state is correct`() = runTest {
        val expectedState = createInitialState()

        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `when location is available and venues are fetched, update state accordingly`() = runTest {
        val expectedPlaces = listOf(createPlace())
        whenever(placesRepository.getNearbyVenues(any(), any())) doReturn PlacesResult.Success(
            expectedPlaces
        )

        val expectedLocation = Location(1.0, 2.0)

        val expectedState = createInitialState().copy(
            mapState = MapState(
                userLocation = expectedLocation,
                nearbyVenues = expectedPlaces
            ),
            venuesState = VenuesState(isLoading = false, hasError = false),
            hasLocationError = false,
            shouldCheckLocationPermission = false,
            shouldFetchLocation = false
        )

        viewModel.uiState.test {
            // Call events in order
            viewModel.onLocationPermissionGranted()
            viewModel.onLocationAvailable(expectedLocation)

            val mostRecent = expectMostRecentItem()
            assertEquals(expectedState, mostRecent)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when there is an error getting location, update state accordingly`() = runTest {
        val expectedState = createInitialState().copy(hasLocationError = true)

        // Simulate location error
        viewModel.onLocationError()

        assertEquals(expectedState, viewModel.uiState.first())
    }

    private fun createInitialState(): HomeUiState = HomeUiState(
        mapState = MapState(userLocation = null, nearbyVenues = null),
        venuesState = VenuesState(isLoading = false, hasError = false),
        hasLocationError = false,
        shouldCheckLocationPermission = true,
        shouldFetchLocation = false
    )
}
