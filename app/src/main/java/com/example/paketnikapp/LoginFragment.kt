package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: MyApplication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app = (activity as MainActivity).getApp()

        loginButton()
        createAccountButton()
    }

    private fun loginButton() {
        binding.buttonLoginOnLogin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            val scope = CoroutineScope(Dispatchers.Main)

            scope.launch {
                if (HttpCalls.login(username, password, app)) {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Uspešno prijavljeni", Toast.LENGTH_SHORT).show()
                    }

                    val action = LoginFragmentDirections.actionFragmentLoginToHomeFragment()
                    findNavController().navigate(action)
                }
                else {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Nepravilno ime ali geslo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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