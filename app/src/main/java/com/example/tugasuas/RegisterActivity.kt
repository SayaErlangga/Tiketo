package com.example.tugasuas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.tugasuas.data.User
import com.example.tugasuas.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollectionRef = firestore.collection("users")
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding){
            btnRegister.setOnClickListener {
                val username = registerUsername.text.toString()
                val email = registerEmail.text.toString()
                val phone = registerPhone.text.toString()
                val password = registerPassword.text.toString()
                val role = "user"
                if (registerConfirmPassword.text.toString() == password){
                    val addUser = User(username = username, email = email, phone = phone, password = password, role = role)
                    newUser(addUser)
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "$registerPassword $registerConfirmPassword", Toast.LENGTH_SHORT).show()
                }

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