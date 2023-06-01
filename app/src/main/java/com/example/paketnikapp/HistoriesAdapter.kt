package com.example.paketnikapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.History

class HistoriesAdapter(private var data: List<History>): RecyclerView.Adapter<HistoriesAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvBoxID: TextView = itemView.findViewById(R.id.recycleViewItem_BoxID)
        val tvDate: TextView = itemView.findViewById(R.id.recycleViewItem_Date)
        val tvOpen: TextView = itemView.findViewById(R.id.recycleViewItem_Open)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_view_item, parent, false)
        return HistoryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history: History = data[position]

        holder.tvBoxID.text = "Box ID: ${history.parentMailBox}"
        holder.tvDate.text = history.date
        holder.tvOpen.text = history.open

        val textColor = if (history.open == "Successful") {
            ContextCompat.getColor(holder.itemView.context, R.color.green)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.red)
        }

        holder.tvOpen.setTextColor(textColor)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}