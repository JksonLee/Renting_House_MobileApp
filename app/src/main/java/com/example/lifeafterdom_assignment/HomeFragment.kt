package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.data.Rooms
import com.example.lifeafterdom_assignment.dataAdaptor.RoomsAdaptor

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Display Recycle View of Others
        val roomList2 : List<Rooms> = listOf(
            Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "Is a single room", 4, 2, 1500, "Male", 1),
            Rooms(2, "PV12", "Setapak, 50200 Kuala Lumpur", 540.50, "Is a single room", 3, 2, 1600, "Female", 2),
            Rooms(3, "PV15", "Setapak, 50200 Kuala Lumpur", 640.50, "Is a double room", 4, 2, 1700, "Male", 1),
            Rooms(4, "PV16", "Setapak, 50200 Kuala Lumpur", 740.50, "Is a double room", 5, 2, 1800, "Female", 2),
        )
        val recyclerView2: RecyclerView = view.findViewById(R.id.rvHRecommend)
        recyclerView2.adapter = RoomsAdaptor(roomList2)

        recyclerView2.setLayoutManager(LinearLayoutManager(view.getContext()))

        recyclerView2.setHasFixedSize(true)


        // Display Recycle View of Others
        val roomList : List<Rooms> = listOf(
            Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "Is a single room", 4, 2, 1500, "Male", 1),
            Rooms(2, "PV12", "Setapak, 50200 Kuala Lumpur", 540.50, "Is a single room", 3, 2, 1600, "Female", 2),
            Rooms(3, "PV15", "Setapak, 50200 Kuala Lumpur", 640.50, "Is a double room", 4, 2, 1700, "Male", 1),
            Rooms(4, "PV16", "Setapak, 50200 Kuala Lumpur", 740.50, "Is a double room", 5, 2, 1800, "Female", 2),
        )
        val recyclerView: RecyclerView = view.findViewById(R.id.rvHRoom)
        recyclerView.adapter = RoomsAdaptor(roomList)

        recyclerView.setLayoutManager(LinearLayoutManager(view.getContext()))

        recyclerView.setHasFixedSize(true)


        // Navigation Button
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
