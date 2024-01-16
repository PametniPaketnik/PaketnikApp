package com.example.paketnikapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evo.GA
import com.example.evo.TSP
import com.example.lib.Location
import com.example.lib.LocationCity
import com.example.paketnikapp.databinding.FragmentTSPAlgorithmBinding
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Arrays


class TSPAlgorithmFragment : Fragment(R.layout.fragment_t_s_p_algorithm) {
    private var _binding : FragmentTSPAlgorithmBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationAdapter: LocationAdapter
    private val locationsList = mutableListOf<Location>()

    private var isDurationEnabled = false
    private var isDistanceEnabled = false
    private var filePath = ""

    companion object {
        private val newLocationList = mutableListOf<Location>()
        private val DataLocationList = mutableListOf<LocationCity>()
        private var bestPathIndexes: List<Int>? = null

        fun getNewLocationList(): List<Location> {
            return newLocationList
        }

        fun setBestPathIndexes(indexes: List<Int>) {
            bestPathIndexes = indexes
        }

        fun getBestPathIndexes(): List<Int>? {
            return bestPathIndexes
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTSPAlgorithmBinding.inflate(inflater, container, false)
        val view = binding.root

        newLocationList.clear()
        DataLocationList.clear()

        binding.buttonDuration.setOnClickListener {
            isDurationEnabled = !isDurationEnabled
            Timber.tag("TSPAlgorithmFragment").d("Duration is enabled: %s", isDurationEnabled)
            filePath = "duration_matrix.tsp"
        }

        binding.buttonDistance.setOnClickListener {
            isDistanceEnabled = !isDistanceEnabled
            Timber.tag("TSPAlgorithmFragment").d("Distance is enabled: %s", isDistanceEnabled)
            filePath = "distance_matrix.tsp"
        }

        readDataFromTSPFile()
        val recyclerView: RecyclerView = view.findViewById(R.id.RecyclerViewFragmentLocation)
        locationAdapter = LocationAdapter(locationsList) { selectedLocation ->
            selectedLocation.isSelected = !selectedLocation.isSelected
            newLocationList.add(Location(selectedLocation.index, selectedLocation.street, selectedLocation.x, selectedLocation.y))
            DataLocationList.add(LocationCity(selectedLocation.index.toInt(), selectedLocation.x, selectedLocation.y))
            Timber.tag("TSPAlgorithmFragment").d("Selected Location: %s", newLocationList.size)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = locationAdapter

        doneButton(DataLocationList)

        return view
    }

    private fun doneButton(DataLocationList: MutableList<LocationCity>) {
        binding.buttonDone.setOnClickListener {
            val action = TSPAlgorithmFragmentDirections.actionTSPAlgorithmFragmentToMapLocationFragment()

            //print DataLocationList in timber
            for (i in DataLocationList) {
                Timber.tag("TSPAlgorithmFragment").d("DataLocationList: %s", i)
            }

            val algorithmTsp = TSP("", 1000, DataLocationList, filePath)
            val ga = GA(100, 0.8, 0.1)
            val bestPath = ga.execute(algorithmTsp)

            setBestPathIndexes(bestPath.pathIndexes.toList())

            println(bestPath.distance)
            println(Arrays.toString(bestPath.pathIndexes))

            findNavController().navigate(action)
        }
    }

    private fun readDataFromTSPFile() {
        try {
            val inputStream = requireContext().assets.open("realProblem96_data1.tsp")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            var linesToSkip = 4

            while (bufferedReader.readLine().also { line = it } != null) {
                if (linesToSkip > 0) {
                    linesToSkip--
                    continue
                }

                val index = line?.substringBefore(" ")?.trim()
                val fullAddress = line?.substringAfter(" ")?.trim()

                val xLine = bufferedReader.readLine()
                val parts = xLine?.split(" ") ?: emptyList()
                val xCoordinate = parts.getOrNull(0)?.toDoubleOrNull()
                val yCoordinate = parts.getOrNull(1)?.toDoubleOrNull()

                if (index != null && fullAddress != null && xCoordinate != null && yCoordinate != null) {
                    locationsList.add(Location(index, fullAddress, xCoordinate, yCoordinate))
                }

                println("Index: $index, Full Address: $fullAddress, X: $xCoordinate, Y: $yCoordinate")
                println("Number of items in locationsList: ${locationsList.size}")
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