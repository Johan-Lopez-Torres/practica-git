package com.example.location_feature

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.location_feature.model.Message
import com.example.location_feature.model.TruckLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException


class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerDragListener {


    //VARIABLES DE MAPAS
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var userLocationMarker: Marker? = null
    private var userLocationAccuracyCircle: Circle? = null

    //VARIABLES DE GEOFENCE
    private lateinit var geocoder: Geocoder
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceHelper: GeofenceHelper
    private val isDriver: Boolean = false // Cambia esto según sea necesario


    companion object {
        private val GEOFENCE_ID = "SOME_GEOFENCE_ID"
        private val BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002
        private val ACCESS_LOCATION_REQUEST_CODE = 10001
        private const val TAG = "MapsActivity"
        private const val RADIUS = 200f
        private val FINE_LOCATION_ACCESS_REQUEST_CODE = 10001
        private const val LATITUDE = -9.117115
        private const val LONGITUDE = -78.515878
        private const val isDriver: Boolean = true
        private const val LOCATION_REQUEST_CODE = 10001
    }


    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        geocoder = Geocoder(this)

        //geofence in maps
        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 500
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        Log.d(TAG, "Trying to add a geofence")
        try {
            addGeofence(LatLng(LATITUDE, LONGITUDE), RADIUS)
            Log.d(TAG, "Tuviste exito geofence")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add  johan: ${e.message}")
        }

        val database = FirebaseDatabase.getInstance().reference
        val message = Message("John", "Hello, world!")
        database.child("personas").push().setValue(message)



        val database2 = Firebase.database
        val myRef = database2.getReference("message")
        myRef.setValue("AGOSTO")




        if (!isDriver) {

            observeCamionLocation()
        }


    }


    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "onStart: permission granted")
            startLocationUpdates()

        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "Map is ready")
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnMapLongClickListener(this)
        mMap.setOnMarkerDragListener(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            enableUserLocation()
//            zoomToUserLocation()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Log.d(TAG, "onMapReady: Should show request permission rationale")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_LOCATION_REQUEST_CODE
                )
            }
        }
    }




    override fun onMapLongClick(p0: LatLng) {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d(TAG, "Hola onMapLongClick: " + p0.latitude + ", " + p0.longitude)
//            handleMapLongClick(p0)
//        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                )
//            ) {
//                Log.d(TAG, "onMapLongClick: Should show request permission rationale")
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                    BACKGROUND_LOCATION_ACCESS_REQUEST_CODE
//                )
//            }
//        }
    }




    //FUNCIONES DE FIREBASE REALTIME DATABASE
    private fun updateCamionLocation(location: Location) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("camion")

        val camionLocation = TruckLocation(
            latitude = location.latitude,
            longitude = location.longitude
        )

        myRef.setValue(camionLocation).addOnSuccessListener {
            Log.d(MainActivity.TAG, "Ubicación actualizada en Firebase")
        }.addOnFailureListener {
            Log.e(MainActivity.TAG, "Error al actualizar la ubicación en Firebase", it)
        }
    }


    private fun observeCamionLocation() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("camion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val camionLocation = snapshot.getValue(TruckLocation::class.java)

                if (camionLocation != null) {
                    Log.d(TAG, "Ubicación del camión: $camionLocation")
                    updateMapWithCamionLocation(camionLocation.latitude, camionLocation.longitude)
                }else{
                    Log.d(TAG, "No se encontró la ubicación del camión")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    MainActivity.TAG,
                    "Error al leer la ubicación del camión",
                    error.toException()
                )
            }
        })
    }


    private fun updateMapWithCamionLocation(latitude: Double, longitude: Double) {
        val camionLocation = LatLng(latitude, longitude)
        mMap.clear()
        mMap.addMarker(
            MarkerOptions()
                .position(camionLocation)
                .title("Camión de basura")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar)) // Asegúrate de tener el recurso adecuado
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camionLocation, 15f))
    }


//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            super.onLocationResult(locationResult)
//            Log.d(TAG, "onLocationResult: " + locationResult.lastLocation)
//            locationResult.lastLocation?.let { setUserLocationMarker(it) }
//        }
//    }


    private val locationCallback2 = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            Log.d(TAG, "onLocationResult: " + locationResult.lastLocation)
            locationResult.lastLocation?.let {
                if (isDriver) {
                    // Actualiza solo la ubicación del usuario
                    setUserLocationMarker(it)
                    updateCamionLocation(it)
                }else
                    setUserLocationMarker(it)
            }
        }
    }

    private fun setUserLocationMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val iconRes = if (isDriver) R.drawable.redcar else R.drawable.persona // Cambia los recursos según corresponda


        if (userLocationMarker == null) {
            val markerOptions = MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(iconRes))
                .rotation(location.bearing)
                .anchor(0.5f, 0.5f)
            userLocationMarker = mMap.addMarker(markerOptions)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        } else {
            userLocationMarker!!.position = latLng
            userLocationMarker!!.rotation = location.bearing
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        }

//        if (userLocationAccuracyCircle == null) {
//            val circleOptions = CircleOptions()
//                .center(latLng)
//                .strokeWidth(4f)
//                .strokeColor(Color.argb(255, 255, 0, 0))
//                .fillColor(Color.argb(32, 255, 0, 0))
//                .radius(location.accuracy.toDouble())
//            userLocationAccuracyCircle = mMap.addCircle(circleOptions)
//        } else {
//            userLocationAccuracyCircle!!.center = latLng
//            userLocationAccuracyCircle!!.radius = location.accuracy.toDouble()
//        }
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
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback2,
            Looper.getMainLooper()
        )

    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback2)
    }


    private fun enableUserLocation() {
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
        mMap.isMyLocationEnabled = true
    }

    private fun zoomToUserLocation() {
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

        val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
        fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
            Log.d(TAG, "FUNCIONO CORRECTAMENTE")
        }
        locationTask.addOnFailureListener {
            Log.d(TAG, "ERROR PARA EL ZOOM")
        }
    }


    override fun onMarkerDragStart(marker: Marker) {
        Log.d(TAG, "onMarkerDragStart: ")
    }

    override fun onMarkerDrag(marker: Marker) {
        Log.d(TAG, "onMarkerDrag: ")
    }


    override fun onMarkerDragEnd(marker: Marker) {
//        Log.d(TAG, "onMarkerDragEnd: ")
//        val latLng = marker.position
//        try {
//            val addresses: MutableList<Address>? =
//                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
//            if (addresses != null) {
//                if (addresses.isNotEmpty()) {
//                    val address: Address = addresses.get(0)
//                    val streetAddress = address.getAddressLine(0)
//                    marker.title = streetAddress
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                enableUserLocation()
//                zoomToUserLocation()
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission denied")
            }
        }
    }


//    private fun addMarker(latLng: LatLng) {
//        val markerOptions = MarkerOptions().position(latLng)
//        mMap.addMarker(markerOptions)
//    }
//
//    private fun addCircle(latLng: LatLng, radius: Float) {
//        val circleOptions = CircleOptions()
//        circleOptions.center(latLng)
//        circleOptions.radius(radius.toDouble())
//        circleOptions.strokeColor(Color.argb(255, 255, 0, 0))
//        circleOptions.fillColor(Color.argb(64, 255, 0, 0))
//        circleOptions.strokeWidth(4f)
//        mMap.addCircle(circleOptions)
//    }
//
//    private fun handleMapLongClick(latLng: LatLng) {
//        mMap.clear()
//        addMarker(latLng)
//        addCircle(latLng, GEOFENCE_RADIUS)
//        addGeofence(latLng, GEOFENCE_RADIUS)
//    }

    @SuppressLint("VisibleForTests")
    private fun addGeofence(latLng: LatLng, radius: Float) {
        val geofence: Geofence = geofenceHelper.getGeofence(
            GEOFENCE_ID,
            latLng,
            radius,
            Geofence.GEOFENCE_TRANSITION_ENTER
                    or Geofence.GEOFENCE_TRANSITION_DWELL
                    or Geofence.GEOFENCE_TRANSITION_EXIT
        )

        val geofencingRequest: GeofencingRequest = geofenceHelper.getGeofencingRequest(geofence)
        val pendingIntent: PendingIntent? = geofenceHelper.getPendingIntent()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            ?.addOnSuccessListener({
                Log.d(
                    TAG,
                    "onSuccess: Geofence Added..."
                )
            })
            ?.addOnFailureListener({ e ->
                val errorMessage: String? = geofenceHelper.getErrorString(e)
                Log.d(TAG, "onFailure kai: $errorMessage")
            })
    }

}