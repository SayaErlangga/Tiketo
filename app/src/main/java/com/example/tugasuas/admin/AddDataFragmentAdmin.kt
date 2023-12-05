package com.example.tugasuas.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tugasuas.R
import com.example.tugasuas.databinding.FragmentAddDataAdminBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDataAdminBinding.inflate(inflater, container, false)
        val view = binding.root

        val firestore = FirebaseFirestore.getInstance()
        val userCollectionRef = firestore.collection("station")

        binding.btnSimpan.setOnClickListener {
            // Ambil data dari UI
            val stasiunAsal = binding.edtStasiunAsal.text.toString()
            val stasiunTujuan = binding.edtStasiunTujuan.text.toString()
            val harga = binding.edtHargaStasiun.text.toString()

            // Mendapatkan nilai CheckBox
            val fiturMakanSiang = binding.fiturMakanSiang.isChecked
            val fiturDudukDepan = binding.fiturDudukDepan.isChecked

            // Membuat listFitur berdasarkan nilai CheckBox
            val listFitur = mutableListOf<String>()

            if (fiturMakanSiang) {
                listFitur.add("Makan Siang")
            }

            if (fiturDudukDepan) {
                listFitur.add("Duduk Depan")
            }

            // Buat objek Station
            val station = Station(id = "", stasiunAsal = stasiunAsal, stasiunTujuan = stasiunTujuan, harga = harga, listFitur = listFitur)

            // Mendapatkan referensi child baru di dalam referensi utama
            val childReference = userCollectionRef.document()

            // Set nilai objek Station ke dalam database
            childReference.set(station)
            findNavController().apply {
            }.navigateUp()
        }

        return view
    }
}
