package com.example.paketnikapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.lib.Location
import com.example.paketnikapp.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import timber.log.Timber


class MapLocationFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: MapView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().load(view.context, androidx.preference.PreferenceManager.getDefaultSharedPreferences(view.context))

        map = binding.mapview
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val startPoint = GeoPoint(46.5547, 15.6459)
        map.controller.setCenter(startPoint)
        map.controller.setZoom(14.5)

        val locations = TSPAlgorithmFragment.getNewLocationList()
        val numberList = TSPAlgorithmFragment.getBestPathIndexes()
        Timber.tag("MapLocationFragment").d("locations: $numberList")

        if (numberList != null) {
            createConnectedMarkers(locations, numberList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun createConnectedMarkers(locations: List<Location>, numberList : List<Int>) {
        val markers = ArrayList<Marker>()

        for (location in locations) {
            val marker = Marker(map)
            val geoPoint = GeoPoint(location.x, location.y)

            marker.position = geoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_location_24)
            marker.title = location.index
            marker.snippet = location.street

            map.overlays.add(marker)
            markers.add(marker)
        }

        for (i in 0 until numberList.size - 1)
        {
            val startIndex = numberList[i]
            val endIndex = numberList[i + 1]

            val startMarker = markers.firstOrNull { it.title == startIndex.toString() }
            val endMarker = markers.firstOrNull { it.title == endIndex.toString() }

            if (startMarker != null && endMarker != null) {
                val line = Polyline()
                line.addPoint(startMarker.position)
                line.addPoint(endMarker.position)
                line.color = Color.BLUE
                map.overlays.add(line)
            }
        }
    }
}