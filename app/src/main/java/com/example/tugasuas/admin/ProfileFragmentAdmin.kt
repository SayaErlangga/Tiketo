package com.example.tugasuas.admin

import SharedPreferenceManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tugasuas.MainActivity
import com.example.tugasuas.databinding.FragmentProfileAdminBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragmentAdmin.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragmentAdmin : Fragment() {
    private lateinit var binding: FragmentProfileAdminBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileAdminBinding.inflate(inflater, container, false)
        sharedPreferenceManager = SharedPreferenceManager(requireContext())

        with(binding){
            btnLogout.setOnClickListener {
                sharedPreferenceManager.clearLoginDetails()

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        return binding.root
    }
}

