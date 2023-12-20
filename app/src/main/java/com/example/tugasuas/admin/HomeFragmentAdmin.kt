package com.example.tugasuas.admin

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.FragmentHomeAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragmentAdmin : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentHomeAdminBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollectionRef = firestore.collection("station")
    private lateinit var stationAdapter: StationAdapter
    private val stationListLiveData: MutableLiveData<List<Station>> by lazy {
        MutableLiveData<List<Station>>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Home"
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Inflate the layout for this fragment
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val action = HomeFragmentAdminDirections.actionHomeFragmentAdminToAddDataFragmentAdmin()
            btnAddData.setOnClickListener{
                findNavController().navigate(action)
            }
            stationAdapter = StationAdapter(onClickMember = { station ->
                // Handle item click
            }, isAdmin = true)

            stationAdapter.setOnDeleteClickListener { selectedData ->
                deleteStation(selectedData)
            }
            stationAdapter.setOnItemClickListener { selectedData ->

            }
            rvStasiun.layoutManager = LinearLayoutManager(context)
            rvStasiun.adapter = stationAdapter
            }

            observeStation()
            getAllStation()
    }
    private  fun getAllStation() {
        observeStationChanges()
    }

    private fun observeStationChanges() {
        stationCollectionRef.addSnapshotListener{ snapshot, error ->
            if (error != null) {
                Log.d("MainActivity", "Error Listening for budget changes: ", error)
            }
            val station = snapshot?.toObjects(Station::class.java)
            if (station != null) {
                stationListLiveData.postValue(station)
            }
        }
    }

    private fun observeStation(){
        stationListLiveData.observe(viewLifecycleOwner){
                station ->
            stationAdapter.submitList(station)
        }
    }

    private fun deleteStation(station: Station) {
        if (station.id.isEmpty()) {
            Log.d("MainActivity", "Error delete items: budget Id is empty")
            return
        }

        // Show confirmation dialog
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Station")
            .setMessage("Are you sure you want to delete this station?")
            .setPositiveButton("Delete") { dialog, which ->
                // User confirmed, proceed with deletion
                stationCollectionRef.document(station.id).delete().addOnFailureListener {
                    Log.d("Main Activity", "Error deleting budget", it)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}