package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.R
import com.example.location_feature.view.adapter.UsuariosAdapter
import com.example.location_feature.domain.model.Usuarios

class EliminarUsuariosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usuariosAdapter: UsuariosAdapter
    private var firestoreService = FirestoreService()
    private var usuariosList: MutableList<Usuarios> = mutableListOf() // Lista mutable para almacenar usuarios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_eliminar_cuenta) // Tu layout para eliminar usuarios

        recyclerView = findViewById(R.id.recUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cargar tus usuarios desde Firestore
        loadUsuarios()

        // Configurar el adaptador con un listener para manejar la selección
        usuariosAdapter = UsuariosAdapter(usuariosList) { usuario ->
            // Lógica para manejar la selección del usuario
        }
        recyclerView.adapter = usuariosAdapter

        findViewById<Button>(R.id.button_delete_cuenta).setOnClickListener {
            // Lógica para eliminar el usuario seleccionado
            val usuarioSeleccionado = usuariosAdapter.getSelectedUser() // Asegúrate de tener este método
            if (usuarioSeleccionado != null) {
                eliminarUsuario(usuarioSeleccionado)
            } else {
                Toast.makeText(this, "Por favor selecciona un usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUsuarios() {
        firestoreService.getUsuarios { usuarios ->
            usuariosList.clear() // Limpiar la lista antes de agregar nuevos usuarios
            usuariosList.addAll(usuarios) // Agregar nuevos usuarios
            usuariosAdapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
        }
    }

    private fun eliminarUsuario(usuario: Usuarios) {
        firestoreService.eliminarUsuario(usuario.id) { result ->
            if (result) {
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                loadUsuarios() // Recargar la lista de usuarios
            } else {
                Toast.makeText(this, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}