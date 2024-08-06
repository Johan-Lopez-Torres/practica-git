package com.example.location_feature.util

// LocationManager.kt
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.location_feature.view.ui.MainActivity
import com.example.location_feature.view.ui.UbicacionActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

@SuppressLint("VisibleForTests")
class LocationManager(private val activity: Activity, private val fusedLocationProviderClient: FusedLocationProviderClient) {

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private val LOCATION_REQUEST_CODE = 10001

    init {
        locationRequest = LocationRequest.create().apply {
            interval = 4000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    fun startLocationUpdates(callback: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(UbicacionActivity.TAG, "onLocationResult: $location")
                    callback(location)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    fun checkSettingsAndStartLocationUpdates(callback: (Location) -> Unit) {
        val request = locationRequest?.let {
            LocationSettingsRequest.Builder()
                .addLocationRequest(it)
                .build()
        } ?: return

        val client = LocationServices.getSettingsClient(activity)
        val locationSettingsResponseTask = client.checkLocationSettings(request)

        locationSettingsResponseTask.addOnSuccessListener {
            startLocationUpdates(callback)
        }

        locationSettingsResponseTask.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(activity, 1001)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
}
