package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.location_feature.R

class AdminActivity : AppCompatActivity() {

    private lateinit var buttonCreate: Button
    private lateinit var buttonRead: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonEdit: Button // Nuevo botón para editar
    private lateinit var navController: NavController // NavController para navegación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_admin) // Establece el layout

        // Inicializa el NavController
        navController = Navigation.findNavController(this, R.id.nav_graph) // Asegúrate de tener un nav_host_fragment en tu layout

        // Inicializa los botones
        buttonCreate = findViewById(R.id.button_create)
        buttonRead = findViewById(R.id.button_read)
        buttonDelete = findViewById(R.id.button_delete)

        buttonCreate.setOnClickListener {
            navController.navigate(R.id.action_admin_to_crud_crear) // Navegar al fragmento CrearCuentaFragment
        }

        buttonRead.setOnClickListener {
            navController.navigate(R.id.action_admin_to_leer_usuario) // Navegar al fragmento LeerUsuariosFragment
        }

        buttonDelete.setOnClickListener {
            navController.navigate(R.id.action_admin_to_eliminar_cuenta) // Navegar al fragmento EliminarUsuariosFragment
        }


    }
}