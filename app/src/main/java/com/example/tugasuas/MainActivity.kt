package com.example.tugasuas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.tugasuas.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Dashboard"
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager2 = binding.viewPager // Make sure to use the correct ID

        with(binding) {
            viewPager.adapter = TabAdapter(this@MainActivity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> ""
                }
            }.attach()
        }
    }
    fun getViewPager(): ViewPager2 {
        return viewPager2
    }
}
