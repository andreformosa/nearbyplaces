package com.andreformosa.nearbyplaces.feature.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andreformosa.nearbyplaces.R
import com.andreformosa.nearbyplaces.data.api.model.Category
import com.andreformosa.nearbyplaces.data.api.model.Place
import com.andreformosa.nearbyplaces.ui.composable.hueFromHex
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        onLocationAvailable = viewModel::onLocationAvailable,
        onLocationError = viewModel::onLocationError,
        onMyLocationButtonClick = viewModel::onMyLocationButtonClick,
        onLocationPermissionGranted = viewModel::onLocationPermissionGranted,
        onVenuesErrorConsumed = viewModel::onVenuesErrorConsumed,
        onLocationErrorConsumed = viewModel::onLocationErrorConsumed
    )
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onLocationAvailable: (Location) -> Unit,
    onLocationError: () -> Unit,
    onMyLocationButtonClick: () -> Unit,
    onLocationPermissionGranted: () -> Unit,
    onVenuesErrorConsumed: () -> Unit,
    onLocationErrorConsumed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val allPermissionsRevoked =
        permissionState.permissions.size == permissionState.revokedPermissions.size

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.shouldFetchLocation) {
                    // If location permission is granted, get current location
                    if (!allPermissionsRevoked) {
                        GetCurrentLocation(
                            context = LocalContext.current,
                            onLocationSuccess = onLocationAvailable,
                            onLocationError = onLocationError
                        )
                    }
                }

                Box {
                    Map(
                        locationPermissionGranted = !allPermissionsRevoked,
                        mapState = uiState.mapState,
                        onMyLocationButtonClick = onMyLocationButtonClick
                    )
                    if (uiState.venuesState.isLoading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }

                if (uiState.shouldCheckLocationPermission) {
                    RequestPermissionLocation(
                        onPermissionGranted = onLocationPermissionGranted
                    )
                }

                if (uiState.venuesState.hasError) {
                    val message = stringResource(R.string.home_screen_venues_error_message)
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Long
                        )
                        onVenuesErrorConsumed()
                    }
                }

                if (uiState.hasLocationError) {
                    val message = stringResource(R.string.home_screen_location_error_message)
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Long
                        )
                        onLocationErrorConsumed()
                    }
                }
            }
        }
    }
}

@Composable
private fun Map(
    locationPermissionGranted: Boolean,
    mapState: MapState,
    onMyLocationButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userLocation = mapState.userLocation
    val nearbyVenues = mapState.nearbyVenues

    val userLatLng = LatLng(userLocation?.latitude ?: 0.0, userLocation?.longitude ?: 0.0)
    val cameraPositionState = rememberCameraPositionState()
    val mapProperties by remember(locationPermissionGranted) {
        mutableStateOf(MapProperties(isMyLocationEnabled = locationPermissionGranted))
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                tiltGesturesEnabled = false,
            )
        )
    }

    var myLocationButtonClicked by remember { mutableStateOf(false) }

    GoogleMap(
        modifier = modifier
            .fillMaxSize()
            .testTag("map"),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMyLocationButtonClick = {
            myLocationButtonClicked = true
            false
        }
    ) {
        LaunchedEffect(myLocationButtonClicked) {
            if (myLocationButtonClicked) {
                myLocationButtonClicked = false
                onMyLocationButtonClick()
            }
        }

        nearbyVenues?.forEach { place ->
            PlaceMarkerInfoWindow(place)
        }
    }

    // Update camera position whenever userLocation changes
    LaunchedEffect(userLocation) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(userLatLng, 17f)
    }
}

@Composable
private fun PlaceMarkerInfoWindow(
    place: Place,
    modifier: Modifier = Modifier
) {
    MarkerInfoWindow(
        icon = BitmapDescriptorFactory.defaultMarker(hueFromHex(MaterialTheme.colorScheme.primary)),
        state = MarkerState(LatLng(place.geocodes.main.latitude, place.geocodes.main.longitude))
    ) { _ ->
        Box(
            modifier = modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    color = MaterialTheme.colorScheme.primary,
                    width = 2.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .background(color = MaterialTheme.colorScheme.onBackground)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = place.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                formatCategoryDistanceString(place.categories, place.distance)?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                formatAddressString(place.location.address, place.location.locality)?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

private fun formatCategoryDistanceString(
    categories: List<Category>,
    distance: Int
): String? = when {
    categories.firstOrNull() != null -> "${categories.first().name} - ${distance}m away"
    categories.isEmpty() -> "${distance}m away"
    else -> null
}

private fun formatAddressString(address: String?, locality: String?): String? = when {
    address != null && locality != null -> "$address, $locality"
    address != null -> address
    locality != null -> locality
    else -> null
}
