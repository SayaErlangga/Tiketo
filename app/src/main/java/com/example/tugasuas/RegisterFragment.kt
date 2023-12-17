package com.example.tugasuas

import SharedPreferenceManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tugasuas.data.User
import com.example.tugasuas.databinding.FragmentRegisterBinding
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollectionRef = firestore.collection("users")
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding){
            btnRegister.setOnClickListener {
                val username = registerUsername.text.toString()
                val email = registerEmail.text.toString()
                val phone = registerPhone.text.toString()
                val password = registerPassword.text.toString()
                val confirmPassword = registerConfirmPassword.text.toString()
                val checkbox = checkboxRegister.isChecked
                val role = "user"
                if (!checkbox) {
                    Toast.makeText(requireContext(), "Silahkan centang checkbox terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else if (email.isEmpty()) {
                    Toast.makeText(requireContext(), "Masukkan email terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else if (phone.isEmpty()) {
                    Toast.makeText(requireContext(), "Masukkan nomor handphone terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else if (username.isEmpty()) {
                    Toast.makeText(requireContext(), "Masukkan username terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    // Password tidak sesuai
                    Toast.makeText(requireContext(), "Password tidak sesuai dengan konfirmasi password", Toast.LENGTH_SHORT).show()
                } else {
                    // Semua kondisi valid, lanjutkan dengan proses pendaftaran
                    val addUser = User(username = username, email = email, phone = phone, password = password, role = role)
                    newUser(addUser)
                    val viewPager = (activity as? MainActivity)?.getViewPager()
                    viewPager?.currentItem = 0
                }

            }
            btnToLogin.setOnClickListener {
                val viewPager = (activity as? MainActivity)?.getViewPager()
                viewPager?.currentItem = 0
            }
        }
    }

    private fun newUser(user: User) {
        userCollectionRef.add(user).addOnSuccessListener {
                documentReference ->
            val createdUserId = documentReference.id
            user.id = createdUserId
            documentReference.set(user).addOnFailureListener {
                Log.d("Main Activity", "Error updataing user id: ", it)
            }
        }.addOnFailureListener {
            Log.d("Main Activity", "Error adding user id: ", it)
        }
    }
}