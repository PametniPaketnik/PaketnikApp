package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.paketnikapp.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

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

        // Konfigurirajte osmdroid
        Configuration.getInstance().load(view.context, androidx.preference.PreferenceManager.getDefaultSharedPreferences(view.context))

        map = binding.mapview
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val startPoint = GeoPoint(46.5547, 15.6459)
        map.controller.setCenter(startPoint)
        map.controller.setZoom(14.5)

        createMarker()
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

    private fun createMarker() {
        val id = DataHolder.id
        val lat = DataHolder.lat?.toDouble()
        val lng = DataHolder.lng?.toDouble()

        if (lat != null && lng != null) {
            val mailboxMarker = Marker(map)
            mailboxMarker.position = GeoPoint(lat, lng)

            mailboxMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mailboxMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_location_24)

            // Povečanje ikone/markera
            //val markerSize = resources.getDimensionPixelSize(R.dimen.marker_size)
            //mailboxMarker.setInfoWindowAnchor(0.8f, markerSize.toFloat())

            val infoWindow = MarkerWindow(map)
            mailboxMarker.infoWindow = infoWindow

            map.overlays.add(mailboxMarker)
            map.invalidate()
        }
    }


}
