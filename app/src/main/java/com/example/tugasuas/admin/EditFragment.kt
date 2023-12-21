package com.example.tugasuas.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tugasuas.R
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.FragmentAddDataAdminBinding
import com.example.tugasuas.databinding.FragmentEditBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

class EditFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollectionRef = firestore.collection("station")
    private lateinit var binding: FragmentEditBinding
    private var updateId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Add Travel"
        setHasOptionsMenu(true) // Add this line
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(inflater, container, false)
        val view = binding.root
        with(binding){
            val stasiunAsal = arguments?.getString("stasiunAsal")
            val stasiunTujuan = arguments?.getString("stasiunTujuan")
            val harga = arguments?.getString("harga")
            val id = arguments?.getString("id")

            edtStasiunAsal.setText(stasiunAsal)
            edtStasiunTujuan.setText(stasiunTujuan)
            edtHarga.setText(harga)
            updateId = id.toString()


            btnSimpan.setOnClickListener {
                val listFitur = mutableListOf<String>()
                for (buttonId in binding.toggleButtonGroup.checkedButtonIds) {
                    val button: MaterialButton = view.findViewById(buttonId)
                    listFitur.add(button.text.toString())
                }

                val updateStation = Station(stasiunAsal = edtStasiunAsal.text.toString(), stasiunTujuan = edtStasiunTujuan.text.toString(), listFitur = listFitur, harga = edtHarga.text.toString())
                updateData(updateStation)
                updateId = ""
                findNavController().apply {
                }.navigateUp()
            }
        }
        return view
    }
    private fun updateData(station: Station) {
        station.id = updateId
        stationCollectionRef.document(updateId).set(station)
    }
}