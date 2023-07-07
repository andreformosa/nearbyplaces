package com.andreformosa.nearbyplaces.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    @SerialName("categories")
    val categories: List<Category>,
    @SerialName("distance")
    val distance: Int,
    @SerialName("geocodes")
    val geocodes: GeoCodes,
    @SerialName("location")
    val location: Location,
    @SerialName("name")
    val name: String,
    @SerialName("timezone")
    val timezone: String,
)
