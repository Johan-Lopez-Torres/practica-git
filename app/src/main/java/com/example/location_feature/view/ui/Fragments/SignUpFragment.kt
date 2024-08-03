package com.example.location_feature.view.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.location_feature.R
import com.example.location_feature.databinding.FragmentCrearCuentaBinding
import com.example.location_feature.util.Resource
import com.example.location_feature.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentCrearCuentaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCrearCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    handleLoading(isLoading = false)
                    activity?.onBackPressed()
                    Toast.makeText(
                        requireContext(),
                        "Sign up success",
                        Toast.LENGTH_SHORT
                    ).show()
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
            btnCRegister.setOnClickListener {
                handleSignUp()
            }
        }
    }

    private fun handleSignUp() {
        val email = binding.ETCemail.text.toString()
        val password = binding.ETCpassword.text.toString()

        viewModel.signUp(email, password)
    }

    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                btnCRegister.text = ""
                btnCRegister.isEnabled = false
                PBRegister.visibility = View.VISIBLE
            } else {
                PBRegister.visibility = View.GONE
                btnCRegister.text = getString(R.string.buttoncrear)
                btnCRegister.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}