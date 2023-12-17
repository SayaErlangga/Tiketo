import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasuas.admin.FiturAdapter
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.StationLayoutBinding

typealias OnClickMember = (Station) -> Unit

class StationAdapter(private val onClickMember: OnClickMember, private val isAdmin: Boolean) :
    ListAdapter<Station, StationAdapter.ItemMemberViewHolder>(FilmDiffCallback()) {

    private var onItemClick: ((Station) -> Unit)? = null
    private var onDeleteClick: ((Station) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Station) -> Unit) {
        onDeleteClick = listener
    }
    fun setOnItemClickListener(listener: (Station) -> Unit) {
        onItemClick = listener
    }

    inner class ItemMemberViewHolder(private val binding: StationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Station) {
            with(binding) {
                val fiturAdapter = FiturAdapter(data.listFitur)
                txtStasiunAsal.text = data.stasiunAsal
                txtStasiunTujuan.text = data.stasiunTujuan
                rvFitur.layoutManager = LinearLayoutManager(itemView.context)
                rvFitur.adapter = fiturAdapter
                itemView.setOnClickListener {
                    onItemClick?.invoke(data)
                }
                btnDelete.setOnClickListener {
                    onDeleteClick?.invoke(data)
                }
                if (isAdmin) {
                    adminButtonLayout.visibility = View.VISIBLE
                    userFavoriteButtonLayout.visibility = View.GONE
                    btnEdit.setOnClickListener {
                        // Handle edit button click
                    }

                    btnDelete.setOnClickListener {
                        // Handle delete button click
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
        val binding = StationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class FilmDiffCallback : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem == newItem
        }
    }
}