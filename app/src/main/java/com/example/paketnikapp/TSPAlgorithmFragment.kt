package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.paketnikapp.databinding.FragmentTSPAlgorithmBinding


class TSPAlgorithmFragment : Fragment(R.layout.fragment_t_s_p_algorithm) {
    private var _binding : FragmentTSPAlgorithmBinding? = null
    private val binding get() = _binding!!

    private var isDurationEnabled = false
    private var isDistanceEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTSPAlgorithmBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonDuration.setOnClickListener {
            isDurationEnabled = !isDurationEnabled
            println("Duration is enabled: $isDurationEnabled")
        }

        binding.buttonDistance.setOnClickListener {
            isDistanceEnabled = !isDistanceEnabled
            println("Distance is enabled: $isDistanceEnabled")
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}