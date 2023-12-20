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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            btnRegister.setOnClickListener {
                val username = registerUsername.text.toString()
                val email = registerEmail.text.toString()
                val phone = registerPhone.text.toString()
                val password = registerPassword.text.toString()
                val confirmPassword = registerConfirmPassword.text.toString()
                val checkbox = checkboxRegister.isChecked
                val role = "user"

                if (username.isEmpty()) {
                    Toast.makeText(requireContext(), "Please input username", Toast.LENGTH_SHORT).show()
                } else {
                    if (email.isEmpty()) {
                        Toast.makeText(requireContext(), "Please input email", Toast.LENGTH_SHORT).show()
                    } else {
                        if (phone.isEmpty()) {
                            Toast.makeText(requireContext(), "please input phone number", Toast.LENGTH_SHORT).show()
                        } else {
                            if (password != confirmPassword) {
                                Toast.makeText(requireContext(), "passwords do not match", Toast.LENGTH_SHORT).show()
                            } else {
                                if (!checkbox) {
                                    Toast.makeText(requireContext(), "Please tick the checkbox", Toast.LENGTH_SHORT).show()
                                } else {
                                    val addUser = User(username = username, email = email, phone = phone, password = password, role = role)
                                    newUser(addUser)

                                }
                            }
                        }
                    }
                }
            }

            btnToLogin.setOnClickListener {
                val viewPager = (activity as? MainActivity)?.getViewPager()
                viewPager?.currentItem = 0
            }
        }
    }

    private fun newUser(user: User) {
        val userEmail = user.email

        // Check if the email already exists in the database
        userCollectionRef.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Email doesn't exist, proceed with user registration
                    userCollectionRef.add(user)
                        .addOnSuccessListener { documentReference ->
                            val createdUserId = documentReference.id
                            user.id = createdUserId
                            documentReference.set(user)
                                .addOnFailureListener {
                                    Log.d("Main Activity", "Error updating user id: ", it)
                                }
                            Toast.makeText(requireContext(), "User registered successfully", Toast.LENGTH_SHORT).show()
                            val viewPager = (activity as? MainActivity)?.getViewPager()
                            viewPager?.currentItem = 0
                        }
                        .addOnFailureListener {
                            Log.d("Main Activity", "Error adding user id: ", it)
                        }
                } else {
                    // Email already exists, show a toast
                    Toast.makeText(requireContext(), "Email already exists", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Log.d("Main Activity", "Error checking email existence: ", it)
            }
    }
}
