package com.example.tugasuas.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasuas.R

class FiturAdapter(private val listFitur: List<String>) :
    RecyclerView.Adapter<FiturAdapter.FiturViewHolder>() {

    inner class FiturViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFitur: TextView = itemView.findViewById(R.id.txt_fitur)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiturViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fitur, parent, false)
        return FiturViewHolder(view)
    }

    override fun onBindViewHolder(holder: FiturViewHolder, position: Int) {
        holder.txtFitur.text = listFitur[position]
    }

    override fun getItemCount(): Int {
        return listFitur.size
    }
}
