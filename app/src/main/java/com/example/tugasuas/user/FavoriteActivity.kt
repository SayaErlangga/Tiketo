package com.example.tugasuas.user

import FavoriteAdapter
import SharedPreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasuas.data.OrderDao
import com.example.tugasuas.data.OrderRoom
import com.example.tugasuas.data.OrderRoomDatabase
import com.example.tugasuas.databinding.ActivityFavoriteBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteActivity : AppCompatActivity() {
    private lateinit var mOrderDao: OrderDao
    private lateinit var executorService: ExecutorService
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferenceManager = SharedPreferenceManager(this@FavoriteActivity)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = OrderRoomDatabase.getDatabase(this)
        mOrderDao = db!!.orderDao()!!

        with(binding){
            favoriteAdapter = FavoriteAdapter { selectedData ->
                // Handle item click here if needed
            }
            favoriteAdapter.setOnDeleteClickListener { selectedData ->
                delete(selectedData)
            }
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.adapter = favoriteAdapter

            getAllOrder()
        }
    }

    override fun onResume() {
        super.onResume()
        getAllOrder()
    }

    private fun getAllOrder() {
        val userEmail = retrieveUserEmailFromSharedPreferences()

        if (userEmail != null) {
            mOrderDao.getAllOrder(userEmail).observe(this) { favorite ->
                favoriteAdapter.submitList(favorite)
            }
        } else {
            // Handle the case when the user email is null (not logged in or not available)
            // You can show a message or perform any other action as needed
            // For example, you can disable the delete functionality if the user is not logged in
        }
    }

    private fun delete(orderRoom: OrderRoom) {
        AlertDialog.Builder(this)
            .setTitle("Delete Order")
            .setMessage("Are you sure you want to delete this order?")
            .setPositiveButton("Delete") { dialog, which ->
                // User confirmed, proceed with deletion
                executorService.execute {
                    mOrderDao.delete(orderRoom)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun retrieveUserEmailFromSharedPreferences(): String? {
        // Assuming you have a method like this in SharedPreferenceManager
        return sharedPreferenceManager.getUserEmail()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}