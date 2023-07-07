package com.andreformosa.nearbyplaces.feature.home

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.location.LocationRequestCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION]
)
@Composable
fun GetCurrentLocation(
    context: Context,
    onLocationSuccess: (Location) -> Unit,
    onLocationError: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val locationProvider = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }

    DisposableEffect(locationProvider) {
        val cancellationTokenSource = CancellationTokenSource()

        coroutineScope.launch {
            try {
                val location = locationProvider.getCurrentLocation(
                    LocationRequestCompat.QUALITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).await()
                onLocationSuccess(Location(location.latitude, location.longitude))
            } catch (e: Exception) {
                onLocationError()
            }
        }

        onDispose {
            cancellationTokenSource.cancel()
        }
    }
}

data class Location(val latitude: Double, val longitude: Double)
