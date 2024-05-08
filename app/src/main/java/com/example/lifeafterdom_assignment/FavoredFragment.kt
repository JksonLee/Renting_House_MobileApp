package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.data.Rooms
import com.example.lifeafterdom_assignment.dataAdaptor.RoomsAdaptor


class FavoredFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favored, container, false)


        // Display Recycle View
        val roomList : List<Rooms> = listOf(
            Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "Is a single room", 4, 2, 1500, "Male", 1),
            Rooms(2, "PV12", "Setapak, 50200 Kuala Lumpur", 540.50, "Is a single room", 3, 2, 1600, "Female", 2),
            Rooms(3, "PV15", "Setapak, 50200 Kuala Lumpur", 640.50, "Is a double room", 4, 2, 1700, "Male", 1),
            Rooms(4, "PV16", "Setapak, 50200 Kuala Lumpur", 740.50, "Is a double room", 5, 2, 1800, "Female", 2),
        )
        val recyclerView: RecyclerView = view.findViewById(R.id.rvFRomm)
        recyclerView.adapter = RoomsAdaptor(roomList)

        recyclerView.setLayoutManager(LinearLayoutManager(view.getContext()))

        recyclerView.setHasFixedSize(true)


        // Navigation Button
        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)

        btnHome.setOnClickListener{
            val action = FavoredFragmentDirections.actionFavoredFragmentToHomeFragment()

            Navigation.findNavController(view).navigate(action)
        }

        btnFavored.setOnClickListener{
            val action = FavoredFragmentDirections.actionFavoredFragmentSelf()

            Navigation.findNavController(view).navigate(action)
        }

        btnProfile.setOnClickListener{
            val action = FavoredFragmentDirections.actionFavoredFragmentToProfilePageFragment()

            Navigation.findNavController(view).navigate(action)
        }

        return view
    }
}