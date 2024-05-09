package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.data.Rooms
import com.example.lifeafterdom_assignment.dataAdaptor.RoomsAdaptor
import java.util.Locale


class FavoredFragment : Fragment(), RoomsAdaptor.onItemClickListener {
    private lateinit var recyclerViewFavored : RecyclerView
    private lateinit var adaptor : RoomsAdaptor
    private lateinit var roomListFavored : List<Rooms>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favored, container, false)


        // Display Recycle View
//        val roomList : List<Rooms> = listOf(
//            Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "single","Is a single room",  "Male", 1),
//            Rooms(2, "PV12", "Setapak, 50200 Kuala Lumpur", 540.50, "single","Is a single room", "Female", 2),
//            Rooms(3, "PV15", "Setapak, 50200 Kuala Lumpur", 640.50, "double","Is a double room", "Male", 1),
//            Rooms(4, "PV16", "Setapak, 50200 Kuala Lumpur", 740.50, "double","Is a double room",  "Female", 2),
//        )
//        val recyclerView: RecyclerView = view.findViewById(R.id.rvFRomm)
//        recyclerView.adapter = RoomsAdaptor(roomList,this)
//
//        recyclerView.setLayoutManager(LinearLayoutManager(view.getContext()))
//
//        recyclerView.setHasFixedSize(true)

        recyclerViewFavored = view.findViewById(R.id.rvFRoom)

        // Set Size and Context of RecyclerView
        recyclerViewFavored.setHasFixedSize(true)
        recyclerViewFavored.setLayoutManager(LinearLayoutManager(view.getContext()))

        // Add Record Into RecyclerView
        addDataToFavoredList()

        // Display RecyclerView
        adaptor = RoomsAdaptor(roomListFavored,this)
        recyclerViewFavored.adapter = adaptor

        // Filter By Address
        filterByAddress("Setapak")


        // Navigation Button
        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)

        btnHome.setOnClickListener{
            val action =
                com.example.lifeafterdom_assignment.FavoredFragmentDirections.actionFavoredFragmentToHomeFragment()

            Navigation.findNavController(view).navigate(action)
        }

        btnFavored.setOnClickListener{
            val action =
                com.example.lifeafterdom_assignment.FavoredFragmentDirections.actionFavoredFragmentSelf()

            Navigation.findNavController(view).navigate(action)
        }

        btnProfile.setOnClickListener{
            val action =
                com.example.lifeafterdom_assignment.FavoredFragmentDirections.actionFavoredFragmentToProfilePageFragment()

            Navigation.findNavController(view).navigate(action)
        }

        return view
    }

    //Add new Record into Others RecyclerView
    private fun addDataToFavoredList(){
        roomListFavored = listOf(
            Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "single","Is a single room", "Male", 1),
            Rooms(2, "PV12", "Wangsa Maju, 50200 Kuala Lumpur", 540.50, "single","Is a single room", "Female", 2),
            Rooms(3, "PV15", "Wangsa Maju, 50200 Kuala Lumpur", 640.50, "double","Is a double room", "Male", 1),
            Rooms(4, "PV16", "Wangsa Maju, 50200 Kuala Lumpur", 740.50, "double","Is a double room", "Female", 2),
        )}

    //Filter RecyclerView Record By Address
    private fun filterByAddress(newText : String){
        val filteredAddressList = ArrayList<Rooms>()
        for(i in roomListFavored){
            if(i.address.lowercase(Locale.ROOT).contains(newText.lowercase())){
                filteredAddressList.add(i)
            }
        }
        if(filteredAddressList.isEmpty()){
            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
        }
        else{
            adaptor.setFilteredList(filteredAddressList)
        }
    }

    override fun itemClick(position: Int) {
        val room = roomListFavored[position]

        findNavController().navigate(R.id.action_favoredFragment_to_roomDetailFragment)
    }
}