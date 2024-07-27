package com.example.location_feature

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager.TAG
import com.example.location_feature.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
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
import com.google.android.gms.tasks.Task
import java.io.IOException


class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private val TAG = "MapsActivity"
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private val ACCESS_LOCATION_REQUEST_CODE = 10001
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var userLocationMarker: Marker? = null
    private var userLocationAccuracyCircle: Circle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val geocoder = Geocoder(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 500
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnMapLongClickListener(this)
        mMap.setOnMarkerDragListener(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // enableUserLocation()
            // zoomToUserLocation()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_LOCATION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_LOCATION_REQUEST_CODE
                )
            }
        }


    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            Log.d(TAG, "onLocationResult: " + locationResult.lastLocation)
            locationResult.lastLocation?.let { setUserLocationMarker(it) }
        }
    }

    private fun setUserLocationMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)

        if (userLocationMarker == null) {
            // Create a new marker
            val markerOptions = MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar))
                .rotation(location.bearing)
                .anchor(0.5f, 0.5f)
            userLocationMarker = mMap.addMarker(markerOptions)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        } else {
            // Use the previously created marker
            userLocationMarker!!.position = latLng
            userLocationMarker!!.rotation = location.bearing
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        }

        if (userLocationAccuracyCircle == null) {
            val circleOptions = CircleOptions()
                .center(latLng)
                .strokeWidth(4f)
                .strokeColor(Color.argb(255, 255, 0, 0))
                .fillColor(Color.argb(32, 255, 0, 0))
                .radius(location.accuracy.toDouble())
            userLocationAccuracyCircle = mMap.addCircle(circleOptions)
        } else {
            userLocationAccuracyCircle!!.center = latLng
            userLocationAccuracyCircle!!.radius = location.accuracy.toDouble()
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
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        } else {
            // you need to request permissions...
        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
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
        val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
            // mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        Log.d(TAG, "onMapLongClick: $latLng")
        try {
            val addresses: MutableList<Address>? =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses.get(0)
                    val streetAddress = address.getAddressLine(0)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(streetAddress)
                            .draggable(true)
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onMarkerDragStart(marker: Marker) {
        Log.d(TAG, "onMarkerDragStart: ")
    }

    override fun onMarkerDrag(marker: Marker) {
        Log.d(TAG, "onMarkerDrag: ")
    }

    override fun onMarkerDragEnd(marker: Marker) {
        Log.d(TAG, "onMarkerDragEnd: ")
        val latLng = marker.position
        try {
            val addresses: MutableList<Address>? =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses.get(0)
                    val streetAddress = address.getAddressLine(0)
                    marker.title = streetAddress
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation()
                zoomToUserLocation()
            } else {
                // We can show a dialog that permission is not granted...
            }
        }
    }


}