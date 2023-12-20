import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasuas.admin.FiturAdapter
import com.example.tugasuas.data.OrderRoom
import com.example.tugasuas.databinding.StationLayoutBinding
import com.example.tugasuas.user.OnClickMember
import com.google.firebase.firestore.core.View

class FavoriteAdapter(private val onClickMember: OnClickMember) :
    ListAdapter<OrderRoom, FavoriteAdapter.ItemMemberViewHolder>(FilmDiffCallback()) {

    private var onItemClick: ((OrderRoom) -> Unit)? = null
    private var onDeleteClick: ((OrderRoom) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (OrderRoom) -> Unit) {
        onDeleteClick = listener
    }

    inner class ItemMemberViewHolder(private val binding: StationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrderRoom) {
            with(binding) {
                val fiturAdapter = FiturAdapter(data.listFitur)
                txtStasiunAsal.text = data.stasiunAsal
                txtStasiunTujuan.text = data.stasiunTujuan
                rvFitur.layoutManager = LinearLayoutManager(itemView.context)
                rvFitur.adapter = fiturAdapter
                btnDeleteFavorite.visibility = android.view.View.VISIBLE
                itemView.setOnClickListener {
                    onItemClick?.invoke(data)
                }
                btnDeleteFavorite.setOnClickListener {
                    onDeleteClick?.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMemberViewHolder {
        val binding = StationLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class FilmDiffCallback : DiffUtil.ItemCallback<OrderRoom>() {
        override fun areItemsTheSame(oldItem: OrderRoom, newItem: OrderRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderRoom, newItem: OrderRoom): Boolean {
            return oldItem == newItem
        }
    }
}
