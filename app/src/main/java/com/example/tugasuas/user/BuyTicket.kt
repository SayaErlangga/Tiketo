package com.example.tugasuas.user

import SharedPreferenceManager
import android.Manifest
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.tugasuas.R
import android.R as AndroidResources
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tugasuas.data.Order
import com.example.tugasuas.data.Station
import com.example.tugasuas.databinding.FragmentBuyTicketBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class BuyTicket : Fragment() {
    private lateinit var binding: FragmentBuyTicketBinding
    private var basePrice: Int = 0
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollectionRef = firestore.collection("station")
    private val orderCollectionRef = firestore.collection("order")
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private val kelasPriceMap = mapOf(
        "Ekonomi" to 0,
        "Bisnis" to 50000,
        "Eksekutif" to 100000
    )
    companion object {
        private const val CHANNEL_ID = "com.example.tugasuas.notification.channel"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyTicketBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true) // Add this line
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferenceManager = SharedPreferenceManager(requireContext()) // Initialize the sharedPreferenceManager
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Buy Ticket"


        // Mengambil data dari argumen
        val stasiunAsal = arguments?.getString("stasiunAsal")
        val stasiunTujuan = arguments?.getString("stasiunTujuan")
        val harga = arguments?.getString("harga")
        basePrice = harga?.toInt() ?: 0
        val kelas = arrayOf(
            "Ekonomi",
            "Bisnis",
            "Eksekutif"
        )

        // Mengambil data dari Firestore
        stationCollectionRef
            .whereEqualTo("stasiunAsal", stasiunAsal)
            .whereEqualTo("stasiunTujuan", stasiunTujuan)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val station = documents.documents[0].toObject(Station::class.java)

                    // Jika data ada, gunakan listFitur untuk mengatur visibility CheckBox
                    station?.listFitur?.forEach { fitur ->
                        when (fitur) {
                            "Makan Siang" -> binding.buttonMakanSiang.visibility = View.VISIBLE
                            "Duduk Depan" -> binding.buttonDudukDepan.visibility = View.VISIBLE
                            "Sewa Porter" -> binding.buttonSewaPorter.visibility = View.VISIBLE
                            "Taksi ke Hotel" -> binding.buttonTaksiHotel.visibility = View.VISIBLE
                            "Bagasi Koper" -> binding.buttonBagasiKoper.visibility = View.VISIBLE
                            "Duduk Sendiri" -> binding.buttonDudukSendiri.visibility = View.VISIBLE
                            "Extra Snack" -> binding.buttonExtraSnack.visibility = View.VISIBLE
                            // Tambahkan kondisi lain sesuai dengan fitur yang Anda miliki
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }

        with(binding) {
            txtStasiunAsal.text = stasiunAsal
            txtStasiunTujuan.text = stasiunTujuan
            txtHarga.text = harga ?: "0"  // Pastikan harga tidak null, jika null, gunakan "0"

            val adapterKelas = ArrayAdapter<String>(requireContext(), AndroidResources.layout.simple_spinner_item, kelas)
            spinnerKelas.adapter = adapterKelas

            binding.spinnerKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()

                    // Update basePrice berdasarkan pilihan kelas
                    basePrice = (harga?.toInt() ?: 0) + (kelasPriceMap[selectedItem] ?: 0)

                    // Update harga pada TextView
                    binding.txtHarga.text = basePrice.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }


            // Tambahkan pendengar untuk MaterialButtonToggleGroup
            toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                updatePrice()
            }
            btnBuy.setOnClickListener {
                val stasiunAsal = txtStasiunAsal.text.toString()
                val stasiunTujuan = txtStasiunTujuan.text.toString()
                val harga = txtHarga.text.toString()
                val tanggal = txtTanggal.text.toString()
                val listFitur = mutableListOf<String>()
                val selectedKelas = spinnerKelas.selectedItem.toString()
                val userEmail = retrieveUserEmailFromSharedPreferences()

                view?.let { nonNullView ->
                    for (buttonId in binding.toggleButtonGroup.checkedButtonIds) {
                        val button: MaterialButton? = nonNullView.findViewById(buttonId)
                        button?.let {
                            listFitur.add(it.text.toString())
                        }
                    }
                }

                val order = Order(stasiunAsal = stasiunAsal, stasiunTujuan = stasiunTujuan, harga = harga, listFitur = listFitur, tanggal = tanggal, kelas = selectedKelas, user = userEmail)

                newOrder(order)
                showNotification(stasiunAsal, stasiunTujuan)
                findNavController().apply {
                }.navigateUp()
            }

            linearLayout5.setOnClickListener {
                showDatePicker()
            }
        }

        return binding.root
    }

    private fun updatePrice() {
        // Update the price based on each CheckBox state
        var updatedPrice = basePrice

        with(binding) {
            updatedPrice += if (buttonMakanSiang.isChecked) 30000 else 0
            updatedPrice += if (buttonDudukDepan.isChecked) 20000 else 0
            updatedPrice += if (buttonBagasiKoper.isChecked) 20000 else 0
            updatedPrice += if (buttonDudukSendiri.isChecked) 150000 else 0
            updatedPrice += if (buttonExtraSnack.isChecked) 15000 else 0
            updatedPrice += if (buttonTaksiHotel.isChecked) 50000 else 0
            updatedPrice += if (buttonSewaPorter.isChecked) 20000 else 0
            // Tambahkan kondisi lain sesuai dengan fitur yang Anda miliki
        }

        // Update the TextView with the new price
        binding.txtHarga.text = updatedPrice.toString()
    }

    private fun newOrder(order: Order) {
        orderCollectionRef.add(order)
            .addOnSuccessListener { documentReference ->
                val createdOrderId = documentReference.id
                order.id = createdOrderId
                documentReference.set(order)
                    .addOnFailureListener {
                        Log.d("BuyTicket Fragment", "Error updating order id: ", it)
                    }
            }
            .addOnFailureListener {
                Log.d("BuyTicket Fragment", "Error adding order id: ", it)
            }
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        // Tampilkan DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                // Tangani tanggal yang dipilih di sini
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                binding.txtTanggal.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Tampilkan DatePickerDialog saat tombol OK ditekan
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ ->
            val selectedDate = "${
                datePickerDialog.datePicker.dayOfMonth
            }-${
                datePickerDialog.datePicker.month + 1
            }-${
                datePickerDialog.datePicker.year
            }"
            Log.d("BuyTicket", "Selected Date: $selectedDate")
            binding.txtTanggal.text = selectedDate
        }

        datePickerDialog.show()
    }
    private fun retrieveUserEmailFromSharedPreferences(): String? {
        // Assuming you have a method like this in SharedPreferenceManager
        return sharedPreferenceManager.getUserEmail()
    }

    private fun showNotification(stasiunAsal: String?, stasiunTujuan: String?) {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_pin_end) // Replace with your app's icon
            .setContentTitle("Order Successfully")
            .setContentText("Order from $stasiunAsal to $stasiunTujuan already success")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            val notificationManager = NotificationManagerCompat.from(requireContext())
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notificationManager.notify(1, builder.build())
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Order Notifications"
            val descriptionText = "Channel for order notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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