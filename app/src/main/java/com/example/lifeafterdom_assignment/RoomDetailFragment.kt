package com.example.lifeafterdom_assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.example.lifeafterdom_assignment.R

class RoomDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_detail, container, false)

        val ibtnBack : ImageButton = view.findViewById(R.id.ibtnBack)

        ibtnBack.setOnClickListener{
            val action =
                com.example.lifeafterdom_assignment.RoomDetailFragmentDirections.actionRoomDetailFragmentToHomeFragment()

            Navigation.findNavController(view).navigate(action)
        }

        return view
    }
}