package com.example.location_feature

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.location_feature.model.Admin
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject

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
        val jsonArr = JSONArray("[\n" +
                "    {\n" +
                "        'id' : 'admin1',\n" +
                "        'nombres' : 'Deybi Paolo',\n" +
                "        'apellidos' : 'Chávez Sú',\n" +
                "        'email' : 'chavezsu1209@admin.com',\n" +
                "        'clave' : 'DEYBI123'\n" +
                "    },\n" +
                "    {\n" +
                "        'id' : 'admin2',\n" +
                "        'nombres' : 'Johan Kelvin',\n" +
                "        'apellidos' : 'López Torres',\n" +
                "        'email' : 'johanlopezsre@admin.com',\n" +
                "        'clave' : 'JOHAN123'\n" +
                "    },\n" +
                "    {\n" +
                "        'id' : 'admin3',\n" +
                "        'nombres' : 'Daniel Alejandro',\n" +
                "        'apellidos' : 'Martínez Fernández',\n" +
                "        'email' : 'da.martinezf@admin.com',\n" +
                "        'clave' : 'DAMF91011'\n" +
                "    },\n" +
                "    {\n" +
                "        'id' : 'admin4',\n" +
                "        'nombres' : 'Carolina Beatriz',\n" +
                "        'apellidos' : 'González Ruiz',\n" +
                "        'email' : 'cb.gonzalezr@admin.com',\n" +
                "        'clave' : 'CBGR1213'\n" +
                "    },\n" +
                "    {\n" +
                "        'id' : 'admin5',\n" +
                "        'nombres' : 'Fernando José',\n" +
                "        'apellidos' : 'Torres Morales',\n" +
                "        'email' : 'fj.torresm@admin.com',\n" +
                "        'clave' : 'FJTM1415'\n" +
                "    }\n" +
                "]"
        )

        val firebaseFirestore = FirebaseFirestore.getInstance()

        for (i in 0 until jsonArr.length()) {
            val aux = jsonArr.get(i) as JSONObject
            val admin = Admin().apply {
                id = aux.getString("id")
                nombres = aux.getString("nombres")
                apellidos = aux.getString("apellidos")
                email = aux.getString("email")
                clave = aux.getString("clave")
            }

            firebaseFirestore.collection("Admins").document().set(admin)
        }

    }

}
