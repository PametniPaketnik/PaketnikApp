package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton()
        registerButton()
    }

    private fun loginButton() {
        binding.buttonRegisterOnRegister.setOnClickListener {
            val action = RegisterFragmentDirections.actionFragmentRegisterToFragmentLogin()
            findNavController().navigate(action)
        }
    }

    private fun registerButton() {
        /*binding.buttonLogin.setOnClickListener {
            val action = fragment_first_pageDirections.actionFragmentFirstPageToFragmentRegister()
            findNavController().navigate(action)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}