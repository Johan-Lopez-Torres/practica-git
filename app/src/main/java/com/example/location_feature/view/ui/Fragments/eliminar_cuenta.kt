package com.example.location_feature.view.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.databinding.FragmentEliminarCuentaBinding
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.view.adapter.UsuariosAdapter

class eliminar_cuenta : Fragment() {

    private var _binding: FragmentEliminarCuentaBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestoreService: FirestoreService
    private lateinit var usuariosAdapter: UsuariosAdapter
    private val usuarioList: MutableList<Usuario> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEliminarCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestoreService = FirestoreService()

        // Configurar RecyclerView
        setupRecyclerView()

        // Cargar usuarios
        loadUsuarios()

        // Configurar el listener del botón de eliminación
        binding.buttonDeleteCuenta.setOnClickListener {
            val usuarioSeleccionado = usuariosAdapter.getSelectedUser()
            if (usuarioSeleccionado != null) {
                eliminarUsuario(usuarioSeleccionado)
            } else {
                Toast.makeText(requireContext(), "Por favor selecciona un usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recUsuarios.layoutManager = LinearLayoutManager(requireContext())
        usuariosAdapter = UsuariosAdapter(usuarioList) { usuario ->
            // Lógica para manejar la selección del usuario
        }
        binding.recUsuarios.adapter = usuariosAdapter
    }

    private fun loadUsuarios() {
        firestoreService.getUsuarios { usuarios ->
            usuarioList.clear()
            usuarioList.addAll(usuarios)
            usuariosAdapter.notifyDataSetChanged()
        }
    }

    private fun eliminarUsuario(usuario: Usuario) {
        firestoreService.eliminarUsuario(usuario.id) { result ->
            if (result) {
                Toast.makeText(requireContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show()
                loadUsuarios() // Recargar la lista de usuarios
            } else {
                Toast.makeText(requireContext(), "Error al eliminar el usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}