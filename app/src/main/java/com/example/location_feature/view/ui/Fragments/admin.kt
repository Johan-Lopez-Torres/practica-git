package com.example.location_feature.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.location_feature.R
import com.example.location_feature.databinding.FragmentAdminBinding

class admin : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreate.setOnClickListener {
            findNavController().navigate(R.id.action_admin_to_crud_crear)
        }

        binding.buttonRead.setOnClickListener {
            findNavController().navigate(R.id.action_admin_to_leer_usuario)
        }

        binding.buttonDelete.setOnClickListener {
            findNavController().navigate(R.id.action_admin_to_eliminar_cuenta)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
