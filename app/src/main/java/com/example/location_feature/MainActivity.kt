package com.example.location_feature

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
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
import com.example.location_feature.model.Message
import com.example.location_feature.model.TruckLocation
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {




    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var mMap: GoogleMap
     lateinit var database: DatabaseReference

    companion object {
        private const val TAG = "MainActivity"
        private const val LATITUDE = -9.117083
        private const val LONGITUDE = -78.515884
        private const val RADIUS = 100f
        private const val LOCATION_REQUEST_CODE = 10001
    }

    private val geofencePendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
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


        val database = FirebaseDatabase.getInstance().reference
        val message = Message("John", "Hello, world!")
        database.child("personas").push().setValue(message)

        val db = FirebaseFirestore.getInstance()
        val cityRef = db.collection("cities").document("LA")

        val data = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA"
        )

        cityRef.set(data)
            .addOnSuccessListener {
                Log.d("Firestore", "Datos agregados correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al agregar datos", e)
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
            observeCamionLocation()


//            val database = Firebase.database
//            val myRef = database.getReference("message")
//            myRef.setValue("Hello, World!")
//                .addOnSuccessListener {
//                    Log.d(TAG, "Data written successfully!")
//                }
//                .addOnFailureListener { e ->
//                    Log.e(TAG, "Failed to write data", e)
//                }
//            Log.d("despues", "HOLA MUNDO")
        } else {
            askLocationPermission()
        }

//        // Create a new user with a first and last name
//        val user = hashMapOf(
//            "first" to "Ada",
//            "last" to "Lovelace",
//            "born" to 1815
//        )
//
//        val db = Firebase.firestore
//// Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }

// ...



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
            Log.d(TAG, "Ubicación actualizada en Firebase")
        }.addOnFailureListener {
            Log.e(TAG, "Error al actualizar la ubicación en Firebase", it)
        }
    }


    private fun observeCamionLocation() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("camion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val camionLocation = snapshot.getValue(TruckLocation::class.java)

                if (camionLocation != null) {
                    updateMapWithCamionLocation(camionLocation.latitude, camionLocation.longitude)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error al leer la ubicación del camión", error.toException())
            }
        })
    }


    private fun updateMapWithCamionLocation(latitude: Double, longitude: Double) {
        val camionLocation = LatLng(latitude, longitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(camionLocation).title("Camión de basura"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camionLocation, 15f))
    }



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
                updateCamionLocation(location)
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


    fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.RECYCLERVIEW_Notificaciones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notificacionadapter(notificacionesProvider.noticicacionesList)
    }

    fun onCreate1(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_notifecaciones)
        initRecyclerView()

        // Obtén una referencia al botón btRegreo1
        val btnBack: ImageButton = findViewById(R.id.btRegreo1)

        // Configura un listener de clics para el botón
        btnBack.setOnClickListener {
            // Muestra un toast al presionar el botón
            Toast.makeText(this, "Botón de retroceso presionado", Toast.LENGTH_SHORT).show()
        }
    }

    fun onCreate3(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.comentarios_cuenta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comentarios)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Obtén una referencia al botón btnC_back
        val btnBack: ImageButton = findViewById(R.id.btRegreo2)

        // Configura un listener de clics para el botón
        btnBack.setOnClickListener {
            // Muestra un toast al presionar el botón
            Toast.makeText(this, "Botón de retroceso presionado", Toast.LENGTH_SHORT).show()
        }
    }

}


