package com.andreformosa.nearbyplaces.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Icon(
    @SerialName("prefix")
    val prefix: String,
    @SerialName("suffix")
    val suffix: String
)
