package com.example.location_feature.view.ui.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.location_feature.GeofenceHelper
import com.example.location_feature.R
import com.example.location_feature.model.TruckLocation
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class home_mapa : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private var camionLocationMarker: Marker? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var userLocationMarker: Marker? = null
    private var userLocationAccuracyCircle: Circle? = null

    private lateinit var geocoder: Geocoder
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceHelper: GeofenceHelper
    private val isDriver: Boolean = true // Cambia esto según sea necesario

    companion object {
        private val GEOFENCE_ID = "SOME_GEOFENCE_ID"
        private const val TAG = "MapsFragment"
        private const val RADIUS = 200f
        private const val LATITUDE = -9.117115
        private const val LONGITUDE = -78.515878
        private const val LOCATION_REQUEST_CODE = 10001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_mapa, container, false)
    }

    @SuppressLint("VisibleForTests")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geocoder = Geocoder(requireContext())
        geofencingClient = LocationServices.getGeofencingClient(requireContext())
        geofenceHelper = GeofenceHelper(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
            Log.e(TAG, "Failed to add geofence: ${e.message}")
        }



        if (!isDriver) {
            observeCamionLocation()
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
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
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableUserLocation()
            zoomToUserLocation()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Log.d(TAG, "onMapReady: Should show request permission rationale")
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onMapLongClick(p0: LatLng) {
        // Handle map long click if needed
    }

    private fun updateCamionLocation(location: Location) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("camion")

        val camionLocation = TruckLocation(
            latitude = location.latitude,
            longitude = location.longitude
        )

        myRef.setValue(camionLocation).addOnSuccessListener {
            Log.d(TAG, "Ubicación actualizada en Firebase")
        }.addOnFailureListener {
            Log.e(TAG, "Error al actualizar la ubicación en Firebase", it)
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
                } else {
                    Log.d(TAG, "No se encontró la ubicación del camión")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error al leer la ubicación del camión", error.toException())
            }
        })
    }

    private fun updateMapWithCamionLocation(latitude: Double, longitude: Double) {
        val camionLocation = LatLng(latitude, longitude)
        if (camionLocationMarker == null) {
            val markerOptions = MarkerOptions()
                .position(camionLocation)
                .title("Trash Truck")
                .snippet("This is the current location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.truck))
            camionLocationMarker = mMap.addMarker(markerOptions)
            Log.d(TAG, "carrito actual: " + camionLocationMarker!!.position)
        } else {
            camionLocationMarker!!.position = camionLocation
        }
    }

    private val locationCallback2 = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            Log.d(TAG, "onLocationResult firestore: " + locationResult.lastLocation)
            locationResult.lastLocation?.let {
                if (isDriver) {
                    setUserLocationMarker(it)
                    updateCamionLocation(it)
                } else {
                    setUserLocationMarker(it)
                }
            }
        }
    }

    private fun setUserLocationMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val iconRes = if (isDriver) R.drawable.truck else R.drawable.persona
        if (userLocationMarker == null) {
            val markerOptions = MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(iconRes))
                .rotation(location.bearing)
                .anchor(0.5f, 0.5f)
            userLocationMarker = mMap.addMarker(markerOptions)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        } else {
            userLocationMarker!!.position = latLng
            userLocationMarker!!.rotation = location.bearing
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }

    private fun zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
            Log.d(TAG, "FUNCIONO CORRECTAMENTE")
        }.addOnFailureListener {
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
        // Handle marker drag end if needed
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation()
                zoomToUserLocation()
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission denied")
            }
        }
    }

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
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            ?.addOnSuccessListener {
                Log.d(TAG, "onSuccess: Geofence Added...")
            }
            ?.addOnFailureListener { e ->
                val errorMessage: String? = geofenceHelper.getErrorString(e)
                Log.d(TAG, "onFailure kai: $errorMessage")
            }
    }
}
