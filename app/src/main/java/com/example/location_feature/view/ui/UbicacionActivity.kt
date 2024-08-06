package com.example.location_feature.view.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.location_feature.R
import com.example.location_feature.util.LocationManager
import com.google.firebase.FirebaseApp

class UbicacionActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        const val TAG = "UbicacionActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_ubicacionmap)

        FirebaseApp.initializeApp(this)?.let {
            Log.d(TAG, "Firebase initialized successfully")
        } ?: Log.e(TAG, "Firebase initialization failed")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = LocationManager(this, fusedLocationProviderClient)
    }

    override fun onStart() {
        super.onStart()
        locationManager.checkSettingsAndStartLocationUpdates { location ->
            Log.d(TAG, "Received location update: $location")
        }
    }

    override fun onStop() {
        super.onStop()
        locationManager.stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10001) {
            // handle permission result
            locationManager.checkSettingsAndStartLocationUpdates { location ->
                Log.d(TAG, "Received location update: $location")
            }
        }
    }
}
