package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        registerButton()
        existingLoginButton()
    }

    private fun registerButton() {
        binding.buttonRegisterOnRegister.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val email = binding.editEmail.text.toString()
            val firstName = binding.editFirstName.text.toString()
            val lastName = binding.editLastName.text.toString()
            val tel = binding.editTelephone.text.toString()
            val street = binding.editStreet.text.toString()
            val postCode = binding.editPostCode.text.toString()

            val scope = CoroutineScope(Dispatchers.Main)

            scope.launch {
                if(HttpCalls.register(username, password, email, firstName, lastName, tel, street, postCode)) {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Uspešna registracija", Toast.LENGTH_SHORT).show()
                    }

                    val action = RegisterFragmentDirections.actionFragmentRegisterToFragmentLogin()
                    findNavController().navigate(action)
                }
                else {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Napaka pri registraciji", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun existingLoginButton() {
        binding.LinkToLogin.setOnClickListener {
            val action = RegisterFragmentDirections.actionFragmentRegisterToFragmentLogin()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}