package com.andreformosa.nearbyplaces.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreformosa.nearbyplaces.data.api.model.Place
import com.andreformosa.nearbyplaces.data.places.PlacesRepository
import com.andreformosa.nearbyplaces.data.places.PlacesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
) : ViewModel() {

    private val mapState = MutableStateFlow(MapState())
    private val venuesState = MutableStateFlow(VenuesState())
    private val hasLocationError = MutableStateFlow(false)
    private val shouldCheckLocationPermission = MutableStateFlow(true)
    private val shouldFetchLocation = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        mapState,
        venuesState,
        hasLocationError,
        shouldCheckLocationPermission,
        shouldFetchLocation
    ) { mapState, venuesState, hasLocationError, shouldCheckLocationPermission, shouldFetchLocation ->
        HomeUiState(
            mapState = mapState,
            venuesState = venuesState,
            hasLocationError = hasLocationError,
            shouldCheckLocationPermission = shouldCheckLocationPermission,
            shouldFetchLocation = shouldFetchLocation
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(
            mapState = MapState(),
            venuesState = VenuesState(),
            hasLocationError = false,
            shouldCheckLocationPermission = true,
            shouldFetchLocation = false
        )
    )

    fun onLocationAvailable(location: Location) {
        hasLocationError.value = false
        venuesState.value = venuesState.value.copy(isLoading = true)
        shouldFetchLocation.value = false

        // Pre-load user location on map
        mapState.value = MapState(
            userLocation = location,
            nearbyVenues = null
        )

        viewModelScope.launch {
            when (val result =
                placesRepository.getNearbyVenues(location.latitude, location.longitude)) {
                is PlacesResult.Success -> {
                    venuesState.value = venuesState.value.copy(isLoading = false)

                    mapState.value = MapState(
                        userLocation = location,
                        nearbyVenues = result.places.sortedBy { it.distance }
                    )
                }

                PlacesResult.GenericError -> {
                    produceVenuesErrorState()
                    Timber.e("Error fetching nearby venues")
                }
            }
        }
    }

    fun onLocationError() {
        hasLocationError.value = true
    }

    fun onMyLocationButtonClick() {
        shouldCheckLocationPermission.value = false
        shouldFetchLocation.value = true
    }

    fun onLocationPermissionGranted() {
        shouldCheckLocationPermission.value = false
        shouldFetchLocation.value = true
    }

    fun onVenuesErrorConsumed() {
        venuesState.value = venuesState.value.copy(hasError = false)
    }

    fun onLocationErrorConsumed() {
        hasLocationError.value = false
    }

    private fun produceVenuesErrorState() {
        venuesState.value = VenuesState(isLoading = false, hasError = true)
    }
}

data class MapState(
    val userLocation: Location? = null,
    val nearbyVenues: List<Place>? = null
)

data class VenuesState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)

data class HomeUiState(
    val mapState: MapState,
    val venuesState: VenuesState,
    val hasLocationError: Boolean,
    val shouldCheckLocationPermission: Boolean,
    val shouldFetchLocation: Boolean
)
