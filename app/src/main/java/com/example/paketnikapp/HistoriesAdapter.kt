package com.example.paketnikapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.Histories
import com.example.lib.History

class HistoriesAdapter(private var data: Histories): RecyclerView.Adapter<HistoriesAdapter.HistoryViewHolder>() {

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

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history: History = data.getHistoryList()[position]

        holder.tvBoxID.text = "Box ID: ${history.parentMailbox}"
        holder.tvDate.text = history.date.toString()
        holder.tvOpen.text = history.open

        val textColor = if (history.open == "Successful") {
            ContextCompat.getColor(holder.itemView.context, R.color.green)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.red)
        }

        holder.tvOpen.setTextColor(textColor)
    }

    override fun getItemCount(): Int {
        return data.getHistoryList().size
    }
}