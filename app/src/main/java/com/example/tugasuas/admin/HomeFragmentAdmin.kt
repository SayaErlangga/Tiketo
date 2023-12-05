package com.example.tugasuas.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tugasuas.databinding.FragmentHomeAdminBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragmentAdmin.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragmentAdmin : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentHomeAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val action = HomeFragmentAdminDirections.actionHomeFragmentAdminToAddDataFragmentAdmin()
            btnAddData.setOnClickListener{
                findNavController().navigate(action)
            }
        }
    }

}