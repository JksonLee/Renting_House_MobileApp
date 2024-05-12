package com.example.lifeafterdom_assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.lifeafterdom_assignment.R
import kotlin.properties.Delegates

class ProfilePageFragment : Fragment() {
    private val args by navArgs<ProfilePageFragmentArgs>()
    private var userID by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        // Catch Data From Previous Page
        userID = args.userID

        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)

        btnHome.setOnClickListener{
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentToHomeFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        btnFavored.setOnClickListener{
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentToFavoredFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        btnProfile.setOnClickListener{
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentSelf(userID)

            Navigation.findNavController(view).navigate(action)
        }

        return view
    }
}