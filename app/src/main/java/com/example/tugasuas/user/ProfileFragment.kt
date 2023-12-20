package com.example.tugasuas.user

import SharedPreferenceManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tugasuas.MainActivity
import com.example.tugasuas.R
import com.example.tugasuas.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Profile"

        sharedPreferenceManager = SharedPreferenceManager(requireContext())

        with(binding) {
            // Retrieve user data from SharedPreferences
            val userEmail = sharedPreferenceManager.getUserEmail()
            val userName = sharedPreferenceManager.getSavedUserName()
            val userPhone = sharedPreferenceManager.getSavedUserPhone()
            btnLogout.setOnClickListener {
                sharedPreferenceManager.clearLoginDetails()

                // Redirect to the MainActivity
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            btnFavorite.setOnClickListener {
//                findNavController().navigate(R.id.action_profileFragment_to_favoriteFragment)
                val intent = Intent(context, FavoriteActivity::class.java)
                startActivity(intent)
            }
            // Set user data to TextViews
            binding.txtName.text = userName
            binding.txtEmail.text = userEmail
            binding.txtPhone.text = userPhone

        }

        return binding.root
    }
}
