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
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {


    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var mMap: GoogleMap

    companion object {
        const val TAG = "MainActivity"
        private const val LOCATION_REQUEST_CODE = 10001
    }
    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                Log.d(TAG, "onLocationResult: $location")
            }
        }
    }

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        geofencingClient = LocationServices.getGeofencingClient(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create().apply {
            interval = 4000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseApp.initializeApp(this)?.let {
            Log.d(TAG, "Firebase initialized successfully")
        } ?: Log.e(TAG, "Firebase initialization failed")

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            getLastLocation();
            checkSettingsAndStartLocationUpdates()
//            observeCamionLocation()
        } else {
            askLocationPermission()
        }


    }




    //FUNCIONES DE FIREBASE REALTIME DATABASE
//    private fun updateCamionLocation(location: Location) {
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("camion")
//
//        val camionLocation = TruckLocation(
//            latitude = location.latitude,
//            longitude = location.longitude
//        )
//
//        myRef.setValue(camionLocation).addOnSuccessListener {
//            Log.d(TAG, "Ubicación actualizada en Firebase")
//        }.addOnFailureListener {
//            Log.e(TAG, "Error al actualizar la ubicación en Firebase", it)
//        }
//    }
//
//
//    private fun observeCamionLocation() {
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("camion")
//
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val camionLocation = snapshot.getValue(TruckLocation::class.java)
//
//                if (camionLocation != null) {
//                    updateMapWithCamionLocation(camionLocation.latitude, camionLocation.longitude)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e(TAG, "Error al leer la ubicación del camión", error.toException())
//            }
//        })
//    }
//
//
//    private fun updateMapWithCamionLocation(latitude: Double, longitude: Double) {
//        val camionLocation = LatLng(latitude, longitude)
//        mMap.clear()
//        mMap.addMarker(MarkerOptions().position(camionLocation).title("Camión de basura"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camionLocation, 15f))
//    }



    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val locationTask: Task<Location> = fusedLocationProviderClient!!.lastLocation
        locationTask.addOnSuccessListener { location ->
            if (location != null) {
                //We have a location
                Log.d(TAG, "onSuccess: " + location.toString())
                Log.d(TAG, "onSuccess: latitud " + location.getLatitude())
                Log.d(TAG, "onSuccess: longitud " + location.getLongitude())
//                updateCamionLocation(location)
            } else {
                Log.d(TAG, "onSuccess: Location was null...")
            }
        }
        locationTask.addOnFailureListener { e ->
            Log.e(
                TAG,
                "onFailure: " + e.localizedMessage
            )
        }

    }


    private fun checkSettingsAndStartLocationUpdates() {
        val request = locationRequest?.let {
            LocationSettingsRequest.Builder()
                .addLocationRequest(it)
                .build()
        } ?: return

        val client = LocationServices.getSettingsClient(this)
        val locationSettingsResponseTask = client.checkLocationSettings(request)

        locationSettingsResponseTask.addOnSuccessListener {
            startLocationUpdates()
        }

        locationSettingsResponseTask.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@MainActivity, 1001)
                } catch (ex: SendIntentException) {
                    ex.printStackTrace()
                }
            }
        }
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private fun askLocationPermission() {
        // Verifica si el permiso de ubicación ha sido concedido
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Verifica si se debe mostrar una explicación para solicitar el permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Muestra una explicación al usuario sobre por qué se necesita el permiso (esto generalmente implica mostrar un diálogo)
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...")
            }

            // Solicita el permiso de ubicación
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }


    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
                checkSettingsAndStartLocationUpdates()

            } else {
                Log.d(TAG, "onRequestPermissionsResult: Permission denied")
            }
        }
    }

}


