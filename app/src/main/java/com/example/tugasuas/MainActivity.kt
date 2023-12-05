package com.example.tugasuas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugasuas.admin.DashboardAdmin
import com.example.tugasuas.data.User
import com.example.tugasuas.databinding.ActivityMainBinding
import com.example.tugasuas.user.DashboardUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        with(binding){
            btnLogin.setOnClickListener {
                val username = loginUsername.text.toString().trim()
                val password = loginPassword.text.toString().trim()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    loginUser(username, password)
                } else {
                    Toast.makeText(this@MainActivity, "Masukkan Username dan Password", Toast.LENGTH_SHORT).show()
                }
            }
            btnToRegister.setOnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        val usersRef = firestore.collection("users")

        usersRef.whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val user = document.toObject(User::class.java)
                        if (user.password == password) {
                            // Login berhasil
                            Toast.makeText(
                                this@MainActivity,
                                "Login berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (user.role == "user"){
                                val intent = Intent(this@MainActivity, DashboardUser::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@MainActivity, DashboardAdmin::class.java)
                                startActivity(intent)
                            }
                            // Implementasikan aksi yang diinginkan setelah login berhasil
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Username tidak ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                // Handle error
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}