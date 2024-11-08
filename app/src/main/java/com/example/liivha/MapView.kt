package com.example.liivha

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MapView : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 101
    private var isMapInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_activity)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)

        val bckBtn = findViewById<ImageView>(R.id.backBtn)
        bckBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        // Initialize LocationManager to get user's current location
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
            return
        }

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this)

        // Enable the location layer on the map
        googleMap.isMyLocationEnabled = true
    }

    override fun onLocationChanged(location: Location) {
        if (!isMapInitialized) {
            // Get the user's current location
            val userLatLng = LatLng(location.latitude, location.longitude)

            // Add a marker for the user's current position
            googleMap.addMarker(
                MarkerOptions()
                    .position(userLatLng)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            // Move the camera to the user's current position and set a suitable zoom level
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
            isMapInitialized = true

            // Load birding hotspots around the user's current position
            loadBirdingHotspots(userLatLng)
        }
    }

    private fun loadBirdingHotspots(userLatLng: LatLng) {
        // Initialize Retrofit to make the eBird API request
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ebird.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eBirdService = retrofit.create(EBirdService::class.java)
        val call = eBirdService.getNearbyHotspots(
            "4908n0o1v7uo",
            userLatLng.latitude,
            userLatLng.longitude
        )

        call.enqueue(object : retrofit2.Callback<List<EBirdHotspot>> {
            override fun onResponse(
                call: retrofit2.Call<List<EBirdHotspot>>,
                response: retrofit2.Response<List<EBirdHotspot>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    googleMap.clear() // Clear existing markers

                    // Re-add user marker
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .title("You are here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )

                    // Add hotspots markers
                    response.body()!!.forEach { hotspot ->
                        val hotspotLocation = LatLng(hotspot.lat, hotspot.lng)
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(hotspotLocation)
                                .title(hotspot.name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        )
                    }
                } else {
                    Log.d(
                        "MapView",
                        "Failed to retrieve hotspots: ${response.code()} - ${response.errorBody()}"
                    )
                    Toast.makeText(this@MapView, "Failed to retrieve hotspots", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<EBirdHotspot>>, t: Throwable) {
                Log.e("MapView", "Error fetching hotspots: ${t.message}")
                Toast.makeText(
                    this@MapView,
                    "Error fetching hotspots: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, enable location
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    googleMap.isMyLocationEnabled = true
                    // Request location updates again
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        10f,
                        this
                    )
                } else {
                    // Permission denied, show a message to the user
                    Log.d("MapView", "Location permission denied.")
                    Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this) // Stop location updates to save battery
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


    // Retrofit service to fetch eBird data
    interface EBirdService {
        @GET("ref/hotspot/geo")
        fun getNearbyHotspots(
            @Query("key") apiKey: String,
            @Query("lat") latitude: Double,
            @Query("lng") longitude: Double
        ): retrofit2.Call<List<EBirdHotspot>>
    }

    // Data class to handle eBird hotspot response
    data class EBirdHotspot(
        val name: String,
        val lat: Double,
        val lng: Double
    )
}














