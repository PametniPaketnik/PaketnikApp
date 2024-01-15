package com.example.paketnikapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.Location

class LocationAdapter(
    private val locations: List<Location>,
    private val OnItemClick: (Location) -> Unit) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indexTextView: TextView = itemView.findViewById(R.id.recycleViewItem_ID)
        val streetTextView: TextView = itemView.findViewById(R.id.recycleViewItem_Street)
        val image: ImageView = itemView.findViewById(R.id.ball_ImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_locations, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.indexTextView.text = location.index
        holder.streetTextView.text = location.street

        if (location.isSelected) {
            holder.image.setImageResource(R.drawable.baseline_bookmark_added_24)
        } else {
            holder.image.setImageResource(R.drawable.baseline_bookmark_border_24)
        }

        holder.itemView.setOnClickListener {
            OnItemClick(location)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return locations.size
    }


}