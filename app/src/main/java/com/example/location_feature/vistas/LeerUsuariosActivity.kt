package com.example.location_feature

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.adapter.UsuariosAdapter
import com.example.location_feature.model.Usuarios
import com.example.location_feature.vistas.EditarUsuariosActivity

class LeerUsuariosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usuariosAdapter: UsuariosAdapter
    private lateinit var firestoreService: FirestoreService
    private var usuariosList: MutableList<Usuarios> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_leer_usuario)

        recyclerView = findViewById(R.id.recUsuarios_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firestoreService = FirestoreService()
        loadUsuarios()
    }

    private fun loadUsuarios() {
        firestoreService.getUsuarios { usuarios ->
            usuariosList.clear()
            usuariosList.addAll(usuarios)
            usuariosAdapter = UsuariosAdapter(usuariosList) { usuario ->
                // Manejar el clic en un usuario
                val intent = Intent(this, EditarUsuariosActivity::class.java)
                intent.putExtra("usuario", usuario) // Pasar el usuario seleccionado
                startActivity(intent) // Iniciar la actividad de edición
            }
            recyclerView.adapter = usuariosAdapter
        }
    }
}