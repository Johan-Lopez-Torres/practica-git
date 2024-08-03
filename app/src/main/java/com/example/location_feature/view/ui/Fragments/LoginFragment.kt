package com.example.location_feature.view.ui.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.location_feature.R
import com.example.location_feature.databinding.FragmentIniciarSesionBinding
import com.example.location_feature.util.Resource
import com.example.location_feature.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.text.Typography.dagger

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentIniciarSesionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIniciarSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    handleLoading(isLoading = false)
                    findNavController().navigate(R.id.action_iniciar_sesion_to_home_mapa)
                }
                is Resource.Error -> {
                    handleLoading(isLoading = false)
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleLoading(isLoading = true)
                else -> Unit
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            btnLAcceso.setOnClickListener { handleLogin() }
            btnLRegistrarse.setOnClickListener { findNavController().navigate(R.id.action_iniciar_sesion_to_crear_cuenta) }
        }
    }

    private fun handleLogin() {
        val email = binding.ETLemail.text.toString()
        val password = binding.ETLpassword.text.toString()

        viewModel.login(email, password)
    }

    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                btnLAcceso.text = ""
                btnLAcceso.isEnabled = false
                PBLogin.visibility = View.VISIBLE

            } else {
                PBLogin.visibility = View.GONE
                btnLAcceso.text = getString(R.string.buttonacceso)
                btnLAcceso.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}