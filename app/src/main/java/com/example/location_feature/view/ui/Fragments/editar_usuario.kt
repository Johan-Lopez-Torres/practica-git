package com.example.location_feature.view.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.databinding.FragmentEditarUsuarioBinding
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.network.Callback

class editar_usuario : Fragment() {

    private var _binding: FragmentEditarUsuarioBinding? = null
    private val binding get() = _binding!!
    private val firestoreService = FirestoreService()
    private var usuario: Usuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Obtener el Usuario pasado como argumento
        usuario = arguments?.getParcelable("usuario")

        if (usuario != null) {
            binding.editEmail.setText(usuario?.email)
            binding.editPassword.setText(usuario?.password)

            val roles = arrayOf("Administrador", "Conductor", "Ciudadano")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRole.adapter = adapter
            binding.spinnerRole.setSelection(roles.indexOf(usuario?.role))

            binding.buttonUpdate.setOnClickListener {
                val updatedUser = Usuario(
                    id = usuario!!.id,
                    email = binding.editEmail.text.toString(),
                    password = binding.editPassword.text.toString(),
                    role = binding.spinnerRole.selectedItem.toString()
                )

                firestoreService.actualizarUsuario(updatedUser, object : Callback<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        if (result) {
                            Toast.makeText(requireContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                            requireActivity().onBackPressed() // Navegar de vuelta a la pantalla anterior
                        }
                    }

                    override fun onError(e: Exception) {
                        Toast.makeText(requireContext(), "Error al actualizar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(requireContext(), "Usuario no proporcionado", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed() // Volver a la pantalla anterior si no se proporciona el usuario
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}