package com.example.tugasuas

import SharedPreferenceManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.tugasuas.admin.DashboardAdmin
import com.example.tugasuas.data.User
import com.example.tugasuas.databinding.FragmentLoginBinding
import com.example.tugasuas.user.DashboardUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding){
            sharedPreferenceManager = SharedPreferenceManager(requireContext())
            btnLogin.setOnClickListener {
                val username = loginUsername.text.toString().trim()
                val password = loginPassword.text.toString().trim()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    loginUser(username, password)
                } else {
                    Toast.makeText(requireContext(), "Masukkan Username dan Password", Toast.LENGTH_SHORT).show()
                }
            }
            btnToRegister.setOnClickListener {
                val viewPager = (activity as? MainActivity)?.getViewPager()
                viewPager?.currentItem = 1
            }
        }

        if (sharedPreferenceManager.isLoggedIn()) {
            redirectToDashboard(sharedPreferenceManager.getSavedRole())
        }
    }

    private fun redirectToDashboard(role: String?) {
        val intent = when (role) {
            "user" -> Intent(requireContext(), DashboardUser::class.java)
            "admin" -> Intent(requireContext(), DashboardAdmin::class.java)
            else -> Intent(requireContext(), MainActivity::class.java)
        }

        startActivity(intent)
        activity?.finish()
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
                                requireContext(),
                                "Login berhasil",
                                Toast.LENGTH_SHORT
                            ).show()

                            val sharedPreferenceCheckbox = binding.checkboxSp
                            if(sharedPreferenceCheckbox.isChecked){
                                sharedPreferenceManager.saveLoginDetails(username, password, user.role)
                                redirectToDashboard(user.role)
                            } else {
                                redirectToDashboard(user.role)
                            }

                            // Redirect to the appropriate dashboard
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Username tidak ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                // Handle error
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}