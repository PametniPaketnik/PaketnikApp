package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton()
        createAccountButton()
    }

    private fun loginButton() {
        binding.buttonLoginOnLogin.setOnClickListener {
            val action = LoginFragmentDirections.actionFragmentLoginToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun createAccountButton() {
        binding.LinkToRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionFragmentLoginToFragmentRegister()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}