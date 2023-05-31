package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lib.Histories
import com.example.paketnikapp.databinding.FragmentRecycleViewBinding

class RecycleViewFragment : Fragment(R.layout.fragment_recycle_view) {
    private var _binding: FragmentRecycleViewBinding? = null
    private val binding get() = _binding!!
    private var app = Histories.generate(10)
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
        binding.rvHistory.layoutManager = LinearLayoutManager(context)

        adapter = HistoriesAdapter(app)

        binding.rvHistory.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}