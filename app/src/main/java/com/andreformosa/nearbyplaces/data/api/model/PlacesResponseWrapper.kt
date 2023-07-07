package com.andreformosa.nearbyplaces.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponseWrapper(
    @SerialName("results")
    val results: List<Place>
)
