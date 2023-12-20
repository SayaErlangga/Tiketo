package com.example.tugasuas.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasuas.data.User
import com.example.tugasuas.databinding.UserLayoutBinding
import com.google.firebase.firestore.FirebaseFirestore

class UserAdapter :
    ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    private val firestore = FirebaseFirestore.getInstance()

    inner class UserViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            with(binding) {
                txtName.text = user.username
                txtEmail.text = user.email
                checkboxUser.isChecked = user.role == "admin"

                // Handle checkbox change
                checkboxUser.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Checkbox is checked, update role to admin
                        updateUserRole(user.id, "admin")
                    } else {
                        // Checkbox is unchecked, update role to user
                        updateUserRole(user.id, "user")
                    }
                }
            }
        }

        private fun updateUserRole(userId: String, newRole: String) {
            val userRef = firestore.collection("users").document(userId)

            userRef.update("role", newRole)
                .addOnSuccessListener {
                    // Role updated successfully
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
