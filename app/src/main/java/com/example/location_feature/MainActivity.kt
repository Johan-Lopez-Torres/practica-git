package com.example.location_feature

import android.Manifest
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

        val jsonArr = JSONArray(
            "[\n" +
                    "    {\n" +
                    "        'id' : 'horario1',\n" +
                    "        'horaInicio' : '2024-08-01T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-01T09:00:00Z',\n" +
                    "        'id_Camion' : 'camion1',\n" +
                    "        'idRuta' : 'ruta1',\n" +
                    "        'idZona' : 'zona1',\n" +
                    "        'dias' : 'jueves',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario2',\n" +
                    "        'horaInicio' : '2024-08-01T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-01T15:00:00Z',\n" +
                    "        'id_Camion' : 'camion2',\n" +
                    "        'idRuta' : 'ruta2',\n" +
                    "        'idZona' : 'zona2',\n" +
                    "        'dias' : 'jueves',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario3',\n" +
                    "        'horaInicio' : '2024-08-02T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-02T09:00:00Z',\n" +
                    "        'id_Camion' : 'camion3',\n" +
                    "        'idRuta' : 'ruta3',\n" +
                    "        'idZona' : 'zona3',\n" +
                    "        'dias' : 'viernes',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario4',\n" +
                    "        'horaInicio' : '2024-08-02T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-02T15:00:00Z',\n" +
                    "        'id_Camion' : 'camion4',\n" +
                    "        'idRuta' : 'ruta2',\n" +
                    "        'idZona' : 'zona2',\n" +
                    "        'dias' : 'viernes',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario5',\n" +
                    "        'horaInicio' : '2024-08-03T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-03T09:00:00Z',\n" +
                    "        'idCamion' : 'camion5',\n" +
                    "        'idRuta' : 'ruta3',\n" +
                    "        'idZona' : 'zona3',\n" +
                    "        'dias' : 'sabado',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario6',\n" +
                    "        'horaInicio' : '2024-08-03T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-03T15:00:00Z',\n" +
                    "        'idCamion' : 'camion1',\n" +
                    "        'idRuta' : 'ruta1',\n" +
                    "        'idZona' : 'zona1',\n" +
                    "        'dias' : 'sabado',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario7',\n" +
                    "        'horaInicio' : '2024-08-04T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-04T09:00:00Z',\n" +
                    "        'idCamion' : 'camion2',\n" +
                    "        'idRuta' : 'ruta2',\n" +
                    "        'idZona' : 'zona2',\n" +
                    "        'dias' : 'domingo',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario8',\n" +
                    "        'horaInicio' : '2024-08-04T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-04T15:00:00Z',\n" +
                    "        'idCamion' : 'camion3',\n" +
                    "        'idRuta' : 'ruta3',\n" +
                    "        'idZona' : 'zona3',\n" +
                    "        'dias' : 'domingo',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario9',\n" +
                    "        'horaInicio' : '2024-08-05T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-05T09:00:00Z',\n" +
                    "        'idCamion' : 'camion4',\n" +
                    "        'idRuta' : 'ruta2',\n" +
                    "        'idZona' : 'zona2',\n" +
                    "        'dias' : 'lunes',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario10',\n" +
                    "        'horaInicio' : '2024-08-05T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-05T15:00:00Z',\n" +
                    "        'idCamion' : 'camion5',\n" +
                    "        'idRuta' : 'ruta3',\n" +
                    "        'idZona' : 'zona3',\n" +
                    "        'dias' : 'lunes',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario11',\n" +
                    "        'horaInicio' : '2024-08-06T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-06T09:00:00Z',\n" +
                    "        'idCamion' : 'camion1',\n" +
                    "        'idRuta' : 'ruta1',\n" +
                    "        'idZona' : 'zona1',\n" +
                    "        'dias' : 'martes',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario12',\n" +
                    "        'horaInicio' : '2024-08-06T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-06T15:00:00Z',\n" +
                    "        'idCamion' : 'camion2',\n" +
                    "        'idRuta' : 'ruta2',\n" +
                    "        'idZona' : 'zona2',\n" +
                    "        'dias' : 'martes',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario13',\n" +
                    "        'horaInicio' : '2024-08-07T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-07T09:00:00Z',\n" +
                    "        'idCamion' : 'camion3',\n" +
                    "        'idRuta' : 'ruta3',\n" +
                    "        'idZona' : 'zona3',\n" +
                    "        'dias' : 'miercoles',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario14',\n" +
                    "        'horaInicio' : '2024-08-07T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-07T15:00:00Z',\n" +
                    "        'idCamion' : 'camion4',\n" +
                    "        'idRuta' : 'ruta2',\n" +
                    "        'idZona' : 'zona2',\n" +
                    "        'dias' : 'miercoles',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario15',\n" +
                    "        'horaInicio' : '2024-08-08T07:00:00Z',\n" +
                    "        'horaFin' : '2024-08-08T09:00:00Z',\n" +
                    "        'idCamion' : 'camion5',\n" +
                    "        'idRuta' : 'ruta3',\n" +
                    "        'idZona' : 'zona3',\n" +
                    "        'dias' : 'jueves',\n" +
                    "    },\n" +

                    "    {\n" +
                    "        'id' : 'horario16',\n" +
                    "        'horaInicio' : '2024-08-08T13:00:00Z',\n" +
                    "        'horaFin' : '2024-08-08T15:00:00Z',\n" +
                    "        'idCamion' : 'camion1',\n" +
                    "        'idRuta' : 'ruta1',\n" +
                    "        'idZona' : 'zona1',\n" +
                    "        'dias' : 'jueves',\n" +
                    "    },\n" +

                    "]"
        )
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        for (i in 0 until jsonArr.length()) {
            val aux = jsonArr.get(i) as JSONObject
            val horario = Horario().apply {
                id = aux.getString("id")
                horaInicio = dateFormat.parse(aux.getString("horaInicio"))
                horaFin = dateFormat.parse(aux.getString("horaFin"))
                idRuta = aux.getString("idRuta")
                idZona = aux.getString("idZona")
                dias = aux.getString("dias")

            }

            firebaseFirestore.collection("Horarios").document().set(horario)
        }

    }
}
