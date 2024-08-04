package com.example.location_feature

import android.Manifest
import android.content.Intent
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
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.b_notificacion.adapter.notificacionadapter
import com.example.b_notificacion.model.notificacionesProvider
import com.example.location_feature.model.Horario
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main) //
//    }
//}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_calendario) //
 }
}


//    private val TAG = "MainActivity"
//    var LOCATION_REQUEST_CODE = 10001
//    var fusedLocationProviderClient: FusedLocationProviderClient? = null
//    var locationRequest: LocationRequest? = null
//
//    var locationCallback: LocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            for (location in locationResult.locations) {
//                Log.d(TAG, "onLocationResult: $location")
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        locationRequest = LocationRequest.create();
//        locationRequest!!.setInterval(4000);
//        locationRequest!!.setFastestInterval(2000);
//        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
////            getLastLocation();
//            checkSettingsAndStartLocationUpdates()
//        } else {
//            askLocationPermission()
//        }
//    }
//
//    private fun getLastLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        val locationTask: Task<Location> = fusedLocationProviderClient!!.lastLocation
//        locationTask.addOnSuccessListener { location ->
//            if (location != null) {
//                //We have a location
//                Log.d(TAG, "onSuccess: " + location.toString())
//                Log.d(TAG, "onSuccess: " + location.getLatitude())
//                Log.d(TAG, "onSuccess: " + location.getLongitude())
//            } else {
//                Log.d(TAG, "onSuccess: Location was null...")
//            }
//        }
//        locationTask.addOnFailureListener { e ->
//            Log.e(
//                TAG,
//                "onFailure: " + e.localizedMessage
//            )
//        }
//    }
//
//
//    private fun checkSettingsAndStartLocationUpdates() {
//        val request = locationRequest?.let {
//            LocationSettingsRequest.Builder()
//                .addLocationRequest(it).build()
//        }
//        val client = LocationServices.getSettingsClient(this)
//        val locationSettingsResponseTask = request?.let { client.checkLocationSettings(it) }
//        if (locationSettingsResponseTask != null) {
//            locationSettingsResponseTask.addOnSuccessListener { //Settings of device are satisfied and we can start location updates
//                startLocationUpdates()
//            }
//        }
//        if (locationSettingsResponseTask != null) {
//            locationSettingsResponseTask.addOnFailureListener { e ->
//                if (e is ResolvableApiException) {
//                    val apiException = e as ResolvableApiException
//                    try {
//                        apiException.startResolutionForResult(this@MainActivity, 1001)
//                    } catch (ex: SendIntentException) {
//                        ex.printStackTrace()
//                    }
//                }
//            }
//        }
//    }
//
//
//    private fun startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        fusedLocationProviderClient!!.requestLocationUpdates(
//            locationRequest!!,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }
//
//
//    private fun askLocationPermission() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            ) {
//                Log.d(TAG, "askLocationPermission: you should show an alert dialog...")
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    LOCATION_REQUEST_CODE
//                )
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    LOCATION_REQUEST_CODE
//                )
//            }
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        stopLocationUpdates()
//    }
//
//    private fun stopLocationUpdates() {
//        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
////                getLastLocation();
//                checkSettingsAndStartLocationUpdates()
//            } else {
//                //Permission not granted
//
//
//            }
//        }
//    }
//
//    fun onCreate1(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_notifecaciones)
//        initRecyclerView()
//
//        // Obtén una referencia al botón btRegreo1
//        val btnBack: ImageButton = findViewById(R.id.btRegreo1)
//
//        // Configura un listener de clics para el botón
//        btnBack.setOnClickListener {
//            // Muestra un toast al presionar el botón
//            Toast.makeText(this, "Botón de retroceso presionado", Toast.LENGTH_SHORT).show()
//        }
//
//    }
//
//    fun initRecyclerView() {
//        val recyclerView = findViewById<RecyclerView>(R.id.RECYCLERVIEW_Notificaciones)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = notificacionadapter(notificacionesProvider.noticicacionesList)
//    }
//
//    fun onCreate3(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.comentarios_cuenta)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comentarios)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        // Obtén una referencia al botón btnC_back
//        val btnBack: ImageButton = findViewById(R.id.btRegreo2)
//
//        // Configura un listener de clics para el botón
//        btnBack.setOnClickListener {
//            // Muestra un toast al presionar el botón
//            Toast.makeText(this, "Botón de retroceso presionado", Toast.LENGTH_SHORT).show()
//        }
//    }
//}

