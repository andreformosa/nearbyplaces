package com.andreformosa.nearbyplaces.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("icon")
    val icon: Icon,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)
