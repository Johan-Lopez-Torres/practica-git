package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.R
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.network.Callback

class EditarUsuariosActivity : AppCompatActivity() {
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var buttonUpdate: Button
    private lateinit var firestoreService: FirestoreService
    private var usuriousDeselection: Usuario? = null
    private lateinit var navController: NavController // NavController para navegación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_editar_usuario)

        navController = Navigation.findNavController(this, R.id.nav_graph)

        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        spinnerRole = findViewById(R.id.spinner_role)
        buttonUpdate = findViewById(R.id.button_update)

        firestoreService = FirestoreService()

        usuriousDeselection = intent.getParcelableExtra<Usuario>("usuario", Usuario::class.java)

        if (usuriousDeselection != null) {
            editEmail.setText(usuriousDeselection!!.email)
            editPassword.setText(usuriousDeselection!!.password)

            val roles = arrayOf("Administrador", "Conductor", "Ciudadano")
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRole.adapter = adapter

            buttonUpdate.setOnClickListener {
                val updatedUser = Usuario(
                    id = usuriousDeselection!!.id,
                    email = editEmail.text.toString(),
                    password = editPassword.text.toString(),
                    role = spinnerRole.selectedItem.toString()
                )

                firestoreService.actualizarUsuario(updatedUser, object : Callback<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        if (result) {
                            Toast.makeText(this@EditarUsuariosActivity, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                            navController.navigate(R.id.action_editar_usuario_to_leer_usuario) // Navegar de vuelta a AdminActivity
                            finish()
                        }
                    }

                    override fun onError(e: Exception) {
                        Toast.makeText(this@EditarUsuariosActivity, "Error al actualizar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(this, "Usuario no proporcionado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}