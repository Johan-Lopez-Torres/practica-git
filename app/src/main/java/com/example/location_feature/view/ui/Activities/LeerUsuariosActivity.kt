package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.R
import com.example.location_feature.view.adapter.UsuariosAdapter
import com.example.location_feature.domain.model.Usuarios

class LeerUsuariosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usuariosAdapter: UsuariosAdapter
    private lateinit var firestoreService: FirestoreService
    private var usuariosList: MutableList<Usuarios> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_leer_usuario) // Asegúrate de tener este layout

        recyclerView = findViewById(R.id.recUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firestoreService = FirestoreService()
        loadUsuarios()
    }

    private fun loadUsuarios() {
        firestoreService.getUsuarios { usuarios ->
            usuariosList.clear()
            usuariosList.addAll(usuarios)
            usuariosAdapter = UsuariosAdapter(usuariosList) { usuario ->
                // Aquí puedes manejar el clic en un usuario, por ejemplo, abrir una pantalla de detalles
                Toast.makeText(this, "Seleccionaste: ${usuario.email}", Toast.LENGTH_SHORT).show()
            }
            recyclerView.adapter = usuariosAdapter
        }
    }

}