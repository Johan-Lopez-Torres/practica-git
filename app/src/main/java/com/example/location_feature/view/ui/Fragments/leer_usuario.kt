package com.example.location_feature.view.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.databinding.FragmentLeerUsuarioBinding
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.view.adapter.UsuariosAdapter

class leer_usuario : Fragment() {

    private var _binding: FragmentLeerUsuarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestoreService: FirestoreService
    private lateinit var usuariosAdapter: UsuariosAdapter
    private val usuariosList: MutableList<Usuario> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeerUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestoreService = FirestoreService()
        setupRecyclerView()
        loadUsuarios()


    }

    private fun setupRecyclerView() {
        binding.recUsuarios.layoutManager = LinearLayoutManager(requireContext())
        usuariosAdapter = UsuariosAdapter(usuariosList) { usuario ->
            // Manejar el clic en un usuario
            val action = leer_usuarioDirections.actionLeerUsuarioToEditarUsuario(usuario)
            findNavController().navigate(action)
        }
        binding.recUsuarios.adapter = usuariosAdapter
    }

    private fun loadUsuarios() {
        firestoreService.getUsuarios { usuarios ->
            usuariosList.clear()
            usuariosList.addAll(usuarios)
            usuariosAdapter.notifyDataSetChanged() // Actualizar el adaptador
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}