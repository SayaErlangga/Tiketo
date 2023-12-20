package com.example.tugasuas.admin

import SharedPreferenceManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasuas.MainActivity
import com.example.tugasuas.databinding.FragmentProfileAdminBinding
class ProfileFragmentAdmin : Fragment() {
    private lateinit var binding: FragmentProfileAdminBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Profile"
        binding = FragmentProfileAdminBinding.inflate(inflater, container, false)
        sharedPreferenceManager = SharedPreferenceManager(requireContext())

        with(binding){
            val userEmail = sharedPreferenceManager.getUserEmail()
            val userName = sharedPreferenceManager.getSavedUserName()
            val userPhone = sharedPreferenceManager.getSavedUserPhone()
            btnLogout.setOnClickListener {
                sharedPreferenceManager.clearLoginDetails()

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            binding.txtName.text = userName
            binding.txtEmail.text = userEmail
            binding.txtPhone.text = userPhone
        }

        return binding.root
    }
}

