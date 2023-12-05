package com.example.tugasuas.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tugasuas.R
import com.example.tugasuas.databinding.ActivityDashboardUserBinding

class DashboardUser : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigationView.setupWithNavController(navController)
        }
    }
}