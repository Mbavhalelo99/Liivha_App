package com.example.liivha

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MapView : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Load birding hotspots using eBird API
        loadBirdingHotspots()
    }

    private fun loadBirdingHotspots() {
        // Initialize Retrofit to make the eBird API request
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ebird.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eBirdService = retrofit.create(EBirdService::class.java)
        val call = eBirdService.getNearbyHotspots("4908n0o1v7uo", "-25.7479", "28.2293", "50")


        call.enqueue(object : retrofit2.Callback<List<EBirdHotspot>> {
            override fun onResponse(
                call: retrofit2.Call<List<EBirdHotspot>>,
                response: retrofit2.Response<List<EBirdHotspot>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    for (hotspot in response.body()!!) {
                        val location = LatLng(hotspot.lat, hotspot.lng)
                        googleMap.addMarker(MarkerOptions().position(location).title(hotspot.name))
                    }
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                -25.7479,
                                28.2293
                            ), 10f
                        )
                    )
                }
            }

            override fun onFailure(call: retrofit2.Call<List<EBirdHotspot>>, t: Throwable) {
                // Handle error here
            }
        })
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

    // Retrofit service to fetch eBird data
    interface EBirdService {
        @GET("hotspot/geo")
        fun getNearbyHotspots(
            @Query("key") apiKey: String,
            @Query("lat") latitude: String,
            @Query("lng") longitude: String,
            @Query("dist") distance: String
        ): retrofit2.Call<List<EBirdHotspot>>
    }

    // Data class to handle eBird hotspot response
    data class EBirdHotspot(
        val name: String,
        val lat: Double,
        val lng: Double
    )
}
