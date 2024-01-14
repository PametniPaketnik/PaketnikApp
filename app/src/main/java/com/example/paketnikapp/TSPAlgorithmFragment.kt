package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paketnikapp.databinding.FragmentTSPAlgorithmBinding


class TSPAlgorithmFragment : Fragment(R.layout.fragment_t_s_p_algorithm) {
    private var _binding : FragmentTSPAlgorithmBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_t_s_p_algorithm, container, false)
    }


}