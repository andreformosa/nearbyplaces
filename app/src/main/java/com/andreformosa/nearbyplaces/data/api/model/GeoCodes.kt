package com.andreformosa.nearbyplaces.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodes(
    @SerialName("main")
    val main: Main
)

@Serializable
data class Main(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double
)
