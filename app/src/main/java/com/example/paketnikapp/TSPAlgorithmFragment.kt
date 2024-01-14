package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.Location
import com.example.paketnikapp.databinding.FragmentTSPAlgorithmBinding
import java.io.BufferedReader
import java.io.InputStreamReader


class TSPAlgorithmFragment : Fragment(R.layout.fragment_t_s_p_algorithm) {
    private var _binding : FragmentTSPAlgorithmBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationAdapter: LocationAdapter
    private val locationsList = mutableListOf<Location>()

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

        readDataFromTSPFile()
        val recyclerView: RecyclerView = view.findViewById(R.id.RecyclerViewFragmentLocation)
        locationAdapter = LocationAdapter(locationsList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = locationAdapter

        return view
    }

    private fun readDataFromTSPFile() {
        try {
            val inputStream = requireContext().assets.open("realProblem96_data.tsp")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            var linesToSkip = 4

            while (bufferedReader.readLine().also { line = it } != null) {
                if (linesToSkip > 0) {
                    linesToSkip--
                    continue
                }

                val parts = line?.split(" ") ?: emptyList()
                if (parts.isNotEmpty()) {
                    val street = line?.substringAfter(parts[0])?.trim()
                    println("Index: ${parts[0]}, Street: $street")

                    street?.let { locationsList.add(Location(parts[0], it)) }
                    println("Number of items in locationsList: ${locationsList.size}")
                }
            }
            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}