package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.paketnikapp.databinding.FragmentHomeBinding
import io.github.g00fy2.quickie.ScanQRCode

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
            val parts = result.toString().split("/")
            val id = parts[4].removePrefix("0").toIntOrNull()

            if (id != null) {
                Toast.makeText(activity, "ID extracted from QR code: $id", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Failed to extract ID from QR code", Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonScanQRCode.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}