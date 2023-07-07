package com.andreformosa.nearbyplaces.data.places

interface PlacesRepository {

    suspend fun getNearbyVenues(latitude: Double, longitude: Double): PlacesResult
}
