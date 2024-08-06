package com.example.location_feature.view.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ecoferia.network.FirestoreService
import com.example.location_feature.R
import com.example.location_feature.databinding.FragmentCrudCrearBinding
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.network.Callback
import kotlin.random.Random

class crud_crear : Fragment() {

    private var _binding: FragmentCrudCrearBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrudCrearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
