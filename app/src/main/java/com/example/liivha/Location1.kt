package com.example.liivha

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.math.roundToInt

class Location1 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var distanceTextView: TextView
    private lateinit var timeTextView: TextView

    // Destination coordinates
    private val destinationLatLng = LatLng(-25.403236, 28.296906)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location1)

        // Initialize views
        mapView = findViewById(R.id.mapView)
        distanceTextView = findViewById(R.id.distanceTextView)
        timeTextView = findViewById(R.id.timeTextView)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }


        // Get the user's current location and update the map
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)

                // Add markers for user's location and destination
                googleMap.addMarker(MarkerOptions().position(userLatLng).title("Your Location"))
                googleMap.addMarker(
                    MarkerOptions().position(destinationLatLng).title("Destination")
                )

                // Draw a polyline between the user's location and destination
                val polyline: Polyline = googleMap.addPolyline(
                    PolylineOptions()
                        .add(userLatLng, destinationLatLng)
                        .color(R.color.purple_500) // Change to your preferred color
                        .width(8f) // Adjust the width of the polyline
                )

                // Initialize LatLngBounds.Builder
                val boundsBuilder = LatLngBounds.Builder()
                boundsBuilder.include(userLatLng)
                boundsBuilder.include(destinationLatLng)

                // Build the bounds and animate the camera to fit the polyline
                val bounds = boundsBuilder.build()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

                // Calculate the distance and estimated time
                val results = FloatArray(1)
                Location.distanceBetween(
                    userLatLng.latitude,
                    userLatLng.longitude,
                    destinationLatLng.latitude,
                    destinationLatLng.longitude,
                    results
                )

                val distanceInMeters = results[0]
                val distanceInKm = distanceInMeters / 1000
                val distanceInMiles = distanceInKm * 0.621371
                val estimatedTimeInMinutes = (distanceInKm / 60) * 60 // Assuming 60 km/h speed

                // Display the distance and time based on the user's settings
                val distanceUnit = "km" // Replace with actual setting retrieval logic
                val distance = if (distanceUnit == "miles") distanceInMiles else distanceInKm
                val timeInMinutes = estimatedTimeInMinutes.roundToInt()

                distanceTextView.text = "Distance: %.2f $distanceUnit".format(distance)
                timeTextView.text = "Estimated Time: $timeInMinutes minutes"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}


