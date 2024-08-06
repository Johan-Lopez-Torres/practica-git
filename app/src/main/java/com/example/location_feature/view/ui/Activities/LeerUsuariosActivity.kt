package com.example.location_feature.view.ui.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.R
import com.example.location_feature.view.adapter.UsuariosAdapter
import com.example.location_feature.domain.model.Usuario

class LeerUsuariosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usuariosAdapter: UsuariosAdapter
    private lateinit var firestoreService: FirestoreService
    private var usuariosList: MutableList<Usuario> = mutableListOf()
    private lateinit var buttonBackToAdmin: Button // Botón para regresar al AdminFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_leer_usuario)

        recyclerView = findViewById(R.id.recUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonBackToAdmin = findViewById(R.id.btn_back_leer) // Inicializa el botón

        firestoreService = FirestoreService()
        loadUsuarios()

        // Configura el listener del botón
        buttonBackToAdmin.setOnClickListener {
            // Navegar de vuelta al AdminFragment
            finish() // Finaliza la actividad actual para volver a la anterior
        }
    }

    private fun loadUsuarios() {
        firestoreService.getUsuarios { usuarios ->
            usuariosList.clear()
            usuariosList.addAll(usuarios)
            usuariosAdapter = UsuariosAdapter(usuariosList) { usuario ->
                // Manejar el clic en un usuario
                val intent = Intent(this, EditarUsuariosActivity::class.java)
                intent.putExtra("Usuario", usuario) // Pasar el usuario seleccionado
                startActivity(intent) // Iniciar la actividad de edición
            }
            recyclerView.adapter = usuariosAdapter
        }
    }
}