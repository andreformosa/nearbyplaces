package com.andreformosa.nearbyplaces.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    @SerialName("address")
    val address: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("locality")
    val locality: String? = null,
    @SerialName("postcode")
    val postcode: String? = null,
)
