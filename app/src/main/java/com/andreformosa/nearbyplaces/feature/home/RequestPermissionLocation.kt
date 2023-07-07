package com.andreformosa.nearbyplaces.feature.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreformosa.nearbyplaces.R
import com.andreformosa.nearbyplaces.ui.theme.NearbyPlacesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionLocation(
    onPermissionGranted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val allPermissionsRevoked =
        permissionState.permissions.size == permissionState.revokedPermissions.size

    // If at least one location permission has been granted
    if (!allPermissionsRevoked) {
        onPermissionGranted()
    } else {
        RequestPermissionLocationContent(
            shouldShowRationale = permissionState.shouldShowRationale,
            onLaunchMultiplePermissionRequest = permissionState::launchMultiplePermissionRequest,
            modifier = modifier
        )
    }
}

@Composable
private fun RequestPermissionLocationContent(
    shouldShowRationale: Boolean,
    onLaunchMultiplePermissionRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "location icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        val text = if (shouldShowRationale) {
            // Both permissions revoked, show rationale why the app needs it
            stringResource(R.string.permission_text_location_permission_required_rationale)
        } else {
            // First time user sees this, or if user doesn't want to be asked again
            stringResource(R.string.permission_text_location_permission_required)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLaunchMultiplePermissionRequest
        ) {
            Text(stringResource(R.string.permission_button_allow_access))
        }
    }
}

@Preview
@Composable
private fun RequestPermissionLocationPreview() {
    NearbyPlacesTheme {
        Surface {
            RequestPermissionLocationContent(
                shouldShowRationale = false,
                onLaunchMultiplePermissionRequest = {}
            )
        }
    }
}
