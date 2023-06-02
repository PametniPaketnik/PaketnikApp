package com.example.paketnikapp

import android.widget.TextView
import com.example.paketnikapp.databinding.InfoWindowBinding
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MarkerWindow(private var mapView: MapView) :
    InfoWindow(R.layout.info_window, mapView)
{
    private lateinit var binding: InfoWindowBinding

    override fun onOpen(item: Any?) {
        closeAllInfoWindowsOn(mapView)

        val id = DataHolder.id
        val street = DataHolder.street
        val post = DataHolder.postcode

        binding = InfoWindowBinding.bind(mView)
        binding.idMailbox.text = "Boxid: $id"
        binding.streetMailbox.text = "$street, $post"

        mView.setOnClickListener {
            close()
        }
    }

    override fun onClose() {
        println("Closing...")
    }

    override fun getMapView(): MapView {
        println("To more bit")
        return super.getMapView()
    }
}