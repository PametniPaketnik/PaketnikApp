package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.paketnikapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: MyApplication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app = (activity as MainActivity).getApp()
        val user = app.getUser()

        binding.textViewUsername.text = user.username
        binding.textViewFirstName.text = user.firstName
        binding.textViewLastName.text = user.lastName
        binding.textViewEmail.text = user.email
        logoutButton()
    }

    private fun logoutButton() {
        binding.buttonLogout.setOnClickListener {
            app.deleteUserFile()
            val action = ProfileFragmentDirections.actionProfileFragmentToFragmentFirstPage()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}