package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paketnikapp.databinding.FragmentRecycleViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecycleViewFragment : Fragment(R.layout.fragment_recycle_view) {
    private var _binding: FragmentRecycleViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // app = (activity as MainActivity).getApp()

        // Call the history() function to retrieve the Histories list

        val id = DataHolder.id

        if (id != null) {
            Toast.makeText(activity, "Received ID: $id", Toast.LENGTH_SHORT).show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val histories = HttpCalls.getHistoryById(id.toString()) // Call the history() function from HttpCalls
                adapter = HistoriesAdapter(histories) // Pass the histories list to the adapter
                binding.rvHistory.layoutManager = LinearLayoutManager(context)
                binding.rvHistory.adapter = adapter
            } catch (e: Exception) {
                // Handle any errors that occurred during the HTTP call
                Toast.makeText(context, "Error retrieving histories", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}