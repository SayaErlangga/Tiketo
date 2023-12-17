package com.example.tugasuas.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasuas.admin.FiturAdapter
import com.example.tugasuas.data.Order
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.OrderLayoutBinding
import com.example.tugasuas.databinding.StationLayoutBinding

typealias OnClickMember = (Order) -> Unit

class OrderAdapter(private val onClickMember: OnClickMember, private val isAdmin: Boolean) :
    ListAdapter<Order, OrderAdapter.ItemMemberViewHolder>(OrderDiffCallback()) {

    private var onItemClick: ((Order) -> Unit)? = null
    private var onDeleteClick: ((Order) -> Unit)? = null
    fun setOnDeleteClickListener(listener: (Order) -> Unit) {
        onDeleteClick = listener
    }
    inner class ItemMemberViewHolder(private val binding: OrderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Order) {
            with(binding) {
                val fiturAdapter = FiturAdapter(data.listFitur)
                txtStasiunAsal.text = data.stasiunAsal
                txtStasiunTujuan.text = data.stasiunTujuan
                rvFitur.layoutManager = LinearLayoutManager(itemView.context)
                rvFitur.adapter = fiturAdapter
                if (isAdmin) {
                    adminButtonLayout.visibility = View.VISIBLE
                    userFavoriteButtonLayout.visibility = View.GONE

                    btnDelete.setOnClickListener {
                        onDeleteClick?.invoke(data)

                    }
                } else {
                    adminButtonLayout.visibility = View.GONE
                    userFavoriteButtonLayout.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMemberViewHolder {
        val binding = OrderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}