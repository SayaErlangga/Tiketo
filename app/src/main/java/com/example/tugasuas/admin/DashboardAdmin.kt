package com.example.tugasuas.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tugasuas.R
import com.example.tugasuas.databinding.ActivityDashboardAdminBinding

class DashboardAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            val navController = findNavController(R.id.nav_host_fragment_admin)
            bottomNavigationView.setupWithNavController(navController)
        }
    }
}