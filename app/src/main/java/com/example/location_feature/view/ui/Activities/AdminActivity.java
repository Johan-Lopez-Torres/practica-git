package com.example.location_feature.view.ui.Activities;

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.model.Usuarios
import com.example.location_feature.newor.Callback

public class AdminActivity: AppCompatActivity() {
    private lateinit var buttonCreate: Button
    private lateinit var buttonRead: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonEdit: Button // Nuevo botón para editar

    // Declara otros botones si es necesario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crud_bootom) // Establece el layout

        // Inicializa el botón "Crear"
        buttonCreate = findViewById(R.id.button_create)
        // Inicializa el botón "Leer"
        buttonRead = findViewById(R.id.button_read) // Asegúrate de que el ID coincida con tu layout
        //
        buttonDelete = findViewById(R.id.button_delete)
        //
        buttonEdit = findViewById(R.id.button_update) // Inicializa el botón de editar


        buttonCreate.setOnClickListener {
            // Navegar a la actividad CrearCuentaActivity
            val intent = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }

        buttonRead.setOnClickListener {
            // Navegar a la actividad LeerUsuariosActivity
            val intent = Intent(this, LeerUsuariosActivity::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }

        buttonDelete.setOnClickListener {
            // Navegar a la actividad EliminarUsuariosActivity
            val intent = Intent(this, EliminarUsuariosActivity::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }
        buttonEdit.setOnClickListener {
            // Navegar a la actividad EditarUsuarioActivity
            val intent = Intent(this, EditarUsuariosActivity::class.java)
            startActivity(intent)
        }
    }
}
