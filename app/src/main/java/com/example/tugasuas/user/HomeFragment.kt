package com.example.tugasuas.user

import SharedPreferenceManager
import StationAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasuas.R
import com.example.tugasuas.data.OrderDao
import com.example.tugasuas.data.OrderRoom
import com.example.tugasuas.data.OrderRoomDatabase
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var spinnerAsal: Spinner
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollectionRef = firestore.collection("station")
    private lateinit var stationAdapter: StationAdapter
    private val stationListLiveData: MutableLiveData<List<Station>> by lazy {
        MutableLiveData<List<Station>>()
    }
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var selectedStasiunAsal: String? = null
    private var mOrderDao: OrderDao? = null
    private lateinit var executorService: ExecutorService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Home"
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPreferenceManager = SharedPreferenceManager(requireContext())
        executorService = Executors.newSingleThreadExecutor()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerAsal = binding.spinnerAsal
        mOrderDao = OrderRoomDatabase.getDatabase(requireContext())?.orderDao()

        // Initialize spinner with data
        setupSpinner()

        with(binding) {
            stationAdapter = StationAdapter(onClickMember = { station ->
                // Handle item click
            }, isAdmin = false)

            stationAdapter.setOnItemClickListener { selectedData ->
                // Create a Bundle and add data to it
                val bundle = Bundle()
                bundle.putString("stasiunAsal", selectedData.stasiunAsal)
                bundle.putString("stasiunTujuan", selectedData.stasiunTujuan)
                bundle.putString("harga", selectedData.harga)

                // Use the NavController to navigate with the created bundle
                findNavController().navigate(R.id.action_homeFragment_to_buyTicket, bundle)
            }

            stationAdapter.setOnFavoriteClickListener { selectedData ->
                // Memasukkan data ke dalam Room
                val orderRoom = OrderRoom(
                    stasiunAsal = selectedData.stasiunAsal,
                    stasiunTujuan = selectedData.stasiunTujuan,
                    harga = selectedData.harga,
                    user = retrieveUserEmailFromSharedPreferences().toString(),  // Gantilah dengan data pengguna sesuai kebutuhan
                    listFitur = selectedData.listFitur
                )
                insert(orderRoom)
                Toast.makeText(requireContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            }

            rvStasiun.layoutManager = LinearLayoutManager(context)
            rvStasiun.adapter = stationAdapter
        }

        observeBudgets()
        getAllBudgets()
    }

    private fun setupSpinner() {
        firestore.collection("station")
            .orderBy("stasiunAsal", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val stasiunAsalList =
                    documents.map { it["stasiunAsal"].toString() }.distinct().toMutableList()
                stasiunAsalList.add(0, "Tampilkan Semua")

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    stasiunAsalList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerAsal.adapter = adapter

                // Set default value or handle the initial state as needed
                selectedStasiunAsal = "Tampilkan Semua"
                updateAdapterDataBasedOnSelection()
            }

        spinnerAsal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                selectedStasiunAsal = if (selectedItem == "Tampilkan Semua") null else selectedItem
                updateAdapterDataBasedOnSelection()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing selected
            }
        }
    }

    private fun updateAdapterDataBasedOnSelection() {
        val filteredData = if (selectedStasiunAsal.isNullOrEmpty()) {
            stationListLiveData.value
        } else {
            stationListLiveData.value?.filter { it.stasiunAsal == selectedStasiunAsal }
        }
        stationAdapter.submitList(filteredData)
    }


    private fun getAllBudgets() {
        observeBudgetChanges()
    }

    private fun observeBudgetChanges() {
        stationCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("MainActivity", "Error Listening for budget changes: ", error)
            }
            val movies = snapshot?.toObjects(Station::class.java)
            if (movies != null) {
                stationListLiveData.postValue(movies)
            }
        }
    }

    private fun observeBudgets() {
        stationListLiveData.observe(viewLifecycleOwner) { budgets ->
            stationAdapter.submitList(budgets)
        }
    }
    private fun insert(order: OrderRoom) {
        executorService.execute {
            mOrderDao?.insert(order)
        }
    }

    private fun retrieveUserEmailFromSharedPreferences(): String? {
        // Assuming you have a method like this in SharedPreferenceManager
        return sharedPreferenceManager.getUserEmail()
    }

}
