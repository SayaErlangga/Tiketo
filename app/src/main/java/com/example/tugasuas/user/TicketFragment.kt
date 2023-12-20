package com.example.tugasuas.user

import SharedPreferenceManager
import StationAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasuas.data.Order
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.FragmentTicketBinding
import com.google.firebase.firestore.FirebaseFirestore

class TicketFragment : Fragment() {
    private lateinit var binding: FragmentTicketBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val orderCollectionRef = firestore.collection("order")
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private val orderListLiveData: MutableLiveData<List<Order>> by lazy {
        MutableLiveData<List<Order>>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Order History"
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        sharedPreferenceManager = SharedPreferenceManager(requireContext()) // Initialize the sharedPreferenceManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            orderAdapter = OrderAdapter(onClickMember = { station ->
                // Handle item click
            }, isAdmin = true)

            // Set the delete click listener for the OrderAdapter
            orderAdapter.setOnDeleteClickListener { order ->
                deleteOrder(order)
            }


            rvOrder.layoutManager = LinearLayoutManager(context)
            rvOrder.adapter = orderAdapter
        }

        observeOrder()
        getAllOrder()
    }
    private  fun getAllOrder() {
        observeOrderChanges()
    }

    private fun observeOrderChanges() {
        val userEmail = retrieveUserEmailFromSharedPreferences()

        orderCollectionRef.whereEqualTo("user", userEmail)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.d("TicketFragment", "Error Listening for order changes: ", error)
                    return@addSnapshotListener
                }

                val orders = snapshot?.toObjects(Order::class.java)
                orderListLiveData.value = orders
            }
    }



    private fun observeOrder(){
        orderListLiveData.observe(viewLifecycleOwner){
                stations ->
            orderAdapter.submitList(stations)
        }
    }

    private fun deleteOrder(order: Order) {
        if (order.id.isEmpty()) {
            Log.d("TicketFragment", "Error deleting order: Order Id is empty")
            return
        }

        // Show confirmation dialog
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Order")
            .setMessage("Are you sure you want to delete this order?")
            .setPositiveButton("Delete") { dialog, which ->
                // User confirmed, proceed with deletion
                orderCollectionRef.document(order.id).delete().addOnFailureListener {
                    Log.d("TicketFragment", "Error deleting order", it)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun retrieveUserEmailFromSharedPreferences(): String? {
        // Assuming you have a method like this in SharedPreferenceManager
        return sharedPreferenceManager.getUserEmail()
    }
}