# Nearby Places Demo App

Nearby Places is a demo app which demonstrates modern Android development. It is built using Kotlin, Hilt, Coroutines, Flow, Compose UI, ViewModel and Retrofit. In terms of architecture, this is a single activity application using the MVVM design pattern with a unidirectional data flow (UDF) to manage state and events. The packages are structured in a way that would make it easy to extract their contents into modules if the app had to be more complicated.

Functionality includes showing nearby venues from [Foursquare Places API](https://location.foursquare.com/products/places-api/) and showing them on a Google Map. Clicking pins loads additional information about the location. The current location can be refreshed by clicking the My Location button in the top right.

## Set-up

- Add your [Foursquare Places](https://location.foursquare.com/products/places-api/) API Key to `secrets.properties`
- Add your [Google Maps](https://developers.google.com/maps/documentation/android-sdk/start#get-key) API Key to `maps-secrets.properties`
- Compile!
