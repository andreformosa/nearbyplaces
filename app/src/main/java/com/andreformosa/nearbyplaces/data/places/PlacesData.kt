package com.andreformosa.nearbyplaces.data.places

import com.andreformosa.nearbyplaces.data.api.model.Place

sealed interface PlacesResult {
    data class Success(val places: List<Place>) : PlacesResult
    object GenericError : PlacesResult
}
