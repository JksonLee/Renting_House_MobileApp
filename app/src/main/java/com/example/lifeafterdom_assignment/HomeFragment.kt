package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)

        btnHome.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentSelf()

            Navigation.findNavController(view).navigate(action)
        }

        btnFavored.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToFavoredFragment()

            Navigation.findNavController(view).navigate(action)
        }

        btnProfile.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToProfilePageFragment()

            Navigation.findNavController(view).navigate(action)
        }


        return view

    }
}
