package com.andreformosa.nearbyplaces.factory

import com.andreformosa.nearbyplaces.data.api.model.Category
import com.andreformosa.nearbyplaces.data.api.model.GeoCodes
import com.andreformosa.nearbyplaces.data.api.model.Icon
import com.andreformosa.nearbyplaces.data.api.model.Location
import com.andreformosa.nearbyplaces.data.api.model.Main
import com.andreformosa.nearbyplaces.data.api.model.Place
import kotlin.random.Random

fun createPlace(
    categories: List<Category> = listOf(
        Category(
            Icon("prefix", "suffix"),
            Random.nextInt(),
            "Category Name"
        )
    ),
    distance: Int = 10,
    geocodes: GeoCodes = GeoCodes(Main(1.0, 2.0)),
    location: Location = Location(
        "Address",
        "Country",
        "Locality",
        "POSTCODE",
    ),
    name: String = "Place Name",
    timezone: String = "EST"
) = Place(
    categories,
    distance,
    geocodes,
    location,
    name,
    timezone
)
