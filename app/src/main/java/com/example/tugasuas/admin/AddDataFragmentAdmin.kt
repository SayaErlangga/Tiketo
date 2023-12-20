package com.example.tugasuas.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.FragmentAddDataAdminBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddDataFragmentAdmin.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddDataFragmentAdmin : Fragment() {
    private lateinit var binding: FragmentAddDataAdminBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollectionRef = firestore.collection("station")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Add Travel"
        setHasOptionsMenu(true) // Add this line
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Inflate the layout for this fragment
        binding = FragmentAddDataAdminBinding.inflate(inflater, container, false)
        val view = binding.root



        binding.btnSimpan.setOnClickListener {
            // Ambil data dari UI
            val stasiunAsal = binding.edtStasiunAsal.text.toString()
            val stasiunTujuan = binding.edtStasiunTujuan.text.toString()
            val harga = binding.edtHarga.text.toString()
            val listFitur = mutableListOf<String>()

            for (buttonId in binding.toggleButtonGroup.checkedButtonIds) {
                val button: MaterialButton = view.findViewById(buttonId)
                listFitur.add(button.text.toString())
            }
            // Buat objek Station
            val station = Station(stasiunAsal = stasiunAsal, stasiunTujuan = stasiunTujuan, listFitur = listFitur, harga = harga)

            newStation(station)
            findNavController().apply {
            }.navigateUp()
        }
        return view
    }
    private fun newStation(station: Station) {
        stationCollectionRef.add(station).addOnSuccessListener {
                documentReference ->
            val createdStationId = documentReference.id
            station.id = createdStationId
            documentReference.set(station).addOnFailureListener {
                Log.d("Main Activity", "Error updataing station id: ", it)
            }
        }.addOnFailureListener {
            Log.d("Main Activity", "Error adding station id: ", it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
