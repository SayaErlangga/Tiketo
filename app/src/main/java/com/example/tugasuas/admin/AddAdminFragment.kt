package com.example.tugasuas.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasuas.data.User
import com.example.tugasuas.databinding.FragmentAddAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddAdminFragment : Fragment() {
    private lateinit var binding: FragmentAddAdminBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var userAdapter: UserAdapter
    private val stationCollectionRef = firestore.collection("users")
    private val userListLiveData: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "List User"
        binding = FragmentAddAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            userAdapter = UserAdapter()
            rvUser.layoutManager = LinearLayoutManager(context)
            rvUser.adapter = userAdapter
        }
        observeUser()
        getAllUser()
    }
    private fun getAllUser() {
        observeUserChanges()
    }
    private fun observeUser() {
        userListLiveData.observe(viewLifecycleOwner) { user ->
            userAdapter.submitList(user)
        }
    }
    private fun observeUserChanges() {
        stationCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("MainActivity", "Error Listening for budget changes: ", error)
            }
            val movies = snapshot?.toObjects(User::class.java)
            if (movies != null) {
                userListLiveData.postValue(movies)
            }
        }
    }
}
