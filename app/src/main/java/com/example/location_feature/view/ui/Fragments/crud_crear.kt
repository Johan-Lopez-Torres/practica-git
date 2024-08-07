package com.example.location_feature.view.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.databinding.FragmentCrudCrearBinding
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.network.Callback
import kotlin.random.Random

class crud_crear : Fragment() {

    private var _binding: FragmentCrudCrearBinding? = null
    private val binding get() = _binding!!
    private val firestoreService = FirestoreService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrudCrearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roles = arrayOf("Administrador", "Conductor", "Ciudadano")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRoles.adapter = adapter

        binding.CCREARBOOTOM.setOnClickListener {
            val correo = binding.CCORREO.text.toString()
            val clave = binding.CCLAVE.text.toString()
            val rol = binding.spinnerRoles.selectedItem.toString()

            val id = "usuario" + (Random.nextInt(1, 100))
            val usuario = Usuario(id = id, email = correo, password = clave, role = rol)

            firestoreService.crearUsuario(usuario, object : Callback<Boolean> {
                override fun onSuccess(result: Boolean) {
                    if (result) {
                        Toast.makeText(requireContext(), "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed() // Volver a la pantalla anterior
                    } else {
                        Toast.makeText(requireContext(), "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}