package com.example.location_feature.view.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.Task
import com.example.location_feature.R
import com.example.location_feature.util.LocationManager
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
