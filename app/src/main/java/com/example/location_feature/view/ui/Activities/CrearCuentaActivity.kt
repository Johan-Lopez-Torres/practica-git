package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.R
import com.example.location_feature.domain.model.Usuarios
import com.example.location_feature.network.Callback
import kotlin.random.Random

class CrearCuentaActivity : AppCompatActivity() {
    private lateinit var editTextCorreo: EditText
    private lateinit var editTextClave: EditText
    private lateinit var editTextRol: Spinner // Cambiar de EditText a Spinner
    private lateinit var buttonGuardar: Button
    private val firestoreService = FirestoreService() // Inicializa tu servicio Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_crud_crear) // Asegúrate de que este es tu layout
        editTextCorreo = findViewById(R.id.C_CORREO)
        editTextClave = findViewById(R.id.C_CLAVE)
        editTextRol = findViewById(R.id.spinner_roles)
        buttonGuardar = findViewById(R.id.C_CREAR_BOOTOM)

        val roles = arrayOf("Administrador", "Conductor", "Ciudadano") // Cambia esto por tus roles
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTextRol.adapter = adapter

        buttonGuardar.setOnClickListener {
            val correo = editTextCorreo.text.toString()
            val clave = editTextClave.text.toString()
            val rol = editTextRol.selectedItem.toString()

            val id = "usuario" + (Random.nextInt(1, 100))

            // Crea una instancia de Usuarios
            val usuario = Usuarios(id= id, email = correo, password = clave, role = rol)

            // Llama al método para crear el usuario en Firestore
            firestoreService.crearUsuario(usuario, object : Callback<Boolean> {
                override fun onSuccess(result: Boolean) {
                    if (result) {
                        Toast.makeText(this@CrearCuentaActivity, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                        finish() // Cierra la actividad y regresa a la anterior
                    } else {
                        Toast.makeText(this@CrearCuentaActivity, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(e: Exception) {
                    Toast.makeText(this@CrearCuentaActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}