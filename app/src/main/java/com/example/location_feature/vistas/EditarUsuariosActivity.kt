package com.example.location_feature.vistas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.R
import com.example.location_feature.model.Usuarios
import com.example.location_feature.newor.Callback

class EditarUsuariosActivity : AppCompatActivity() {
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var buttonUpdate: Button
    private lateinit var firestoreService: FirestoreService
    private var usuriousDeselection: Usuarios? = null // Cambiado a nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_editar_usuario)

        // Inicializar las vistas
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        spinnerRole = findViewById(R.id.spinner_roless)
        buttonUpdate = findViewById(R.id.button_update_edit)

        firestoreService = FirestoreService()

        // Recibir el usuario seleccionado del Intent
        usuriousDeselection = intent.getParcelableExtra<Usuarios>("usuario", Usuarios::class.java)

        if (usuriousDeselection != null) {
            // Rellenar los campos con los datos del usuario seleccionado
            editEmail.setText(usuriousDeselection!!.email)
            editPassword.setText(usuriousDeselection!!.password)

            // Configurar el spinner con los roles
            val roles = arrayOf("Administrador", "Conductor", "Ciudadano") // Ejemplo de roles
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRole.adapter = adapter

            // Manejar la actualización del usuario
            buttonUpdate.setOnClickListener {
                val updatedUser = Usuarios(
                    id = usuriousDeselection!!.id,
                    email = editEmail.text.toString(),
                    password = editPassword.text.toString(),
                    role = spinnerRole.selectedItem.toString()
                )

                firestoreService.actualizarUsuario(updatedUser, object : Callback<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        if (result) {
                            Toast.makeText(this@EditarUsuariosActivity, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                            finish() // Cerrar la actividad y volver a la anterior
                        }
                    }

                    override fun onError(e: Exception) {
                        Toast.makeText(this@EditarUsuariosActivity, "Error al actualizar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(this, "Usuario no proporcionado", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad si no hay usuario
        }
    }
}