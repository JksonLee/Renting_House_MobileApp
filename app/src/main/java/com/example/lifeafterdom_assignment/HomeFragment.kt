package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.data.Rooms
import com.example.lifeafterdom_assignment.dataAdaptor.RoomsAdaptor
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var searchRoom : SearchView
    private lateinit var recyclerViewRecommend : RecyclerView
    private lateinit var recyclerViewOthers : RecyclerView
    private lateinit var adaptor: RoomsAdaptor
    private var roomListRecommend = ArrayList<Rooms>()
    private var roomListOthers = ArrayList<Rooms>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Display Recycle View of Others (Using Another Method to do recycle view)
//        val roomList : List<Rooms> = listOf(
//            Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "Is a single room", 4, 2, 1500, "Male", 1),
//            Rooms(2, "PV12", "Setapak, 50200 Kuala Lumpur", 540.50, "Is a single room", 3, 2, 1600, "Female", 2),
//            Rooms(3, "PV15", "Setapak, 50200 Kuala Lumpur", 640.50, "Is a double room", 4, 2, 1700, "Male", 1),
//            Rooms(4, "PV16", "Setapak, 50200 Kuala Lumpur", 740.50, "Is a double room", 5, 2, 1800, "Female", 2),
//        )
//        val recyclerView2: RecyclerView = view.findViewById(R.id.rvHRecommend)
//        recyclerView2.adapter = RoomsAdaptor(roomList)
//
//        recyclerView2.setLayoutManager(LinearLayoutManager(view.getContext()))
//
//        recyclerView2.setHasFixedSize(true)

        recyclerViewRecommend = view.findViewById(R.id.rvHRecommend)

        // Set Size and Context of RecyclerView
        recyclerViewRecommend.setHasFixedSize(true)
        recyclerViewRecommend.setLayoutManager(LinearLayoutManager(view.getContext()))

        // Add Record Into RecyclerView
        addDataToRecommendedList()

        // Display RecyclerView
        adaptor = RoomsAdaptor(roomListRecommend)
        recyclerViewRecommend.adapter = adaptor

        // Filter By Address
        filterByAddress("Setapak")




        // Display Recycle View of Others
//        val roomList2 : List<Rooms> = listOf(
//            Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "Is a single room", 4, 2, 1500, "Male", 1),
//            Rooms(2, "PV12", "Setapak, 50200 Kuala Lumpur", 540.50, "Is a single room", 3, 2, 1600, "Female", 2),
//            Rooms(3, "PV15", "Setapak, 50200 Kuala Lumpur", 640.50, "Is a double room", 4, 2, 1700, "Male", 1),
//            Rooms(4, "PV16", "Setapak, 50200 Kuala Lumpur", 740.50, "Is a double room", 5, 2, 1800, "Female", 2),
//        )
//        val recyclerView: RecyclerView = view.findViewById(R.id.rvHRoom)
//        recyclerView.adapter = RoomsAdaptor(roomList2)
//
//        recyclerView.setLayoutManager(LinearLayoutManager(view.getContext()))
//
//        recyclerView.setHasFixedSize(true)

        recyclerViewOthers = view.findViewById(R.id.rvHRoom)
        searchRoom = view.findViewById(R.id.svRoom)

        // Set Size and Context of RecyclerView
        recyclerViewOthers.setHasFixedSize(true)
        recyclerViewOthers.setLayoutManager(LinearLayoutManager(view.getContext()))

        // Add Record Into RecyclerView
        addDataToOthersList()

        // Display RecyclerView
        adaptor = RoomsAdaptor(roomListOthers)
        recyclerViewOthers.adapter = adaptor

        // Search Room Name
        searchRoom.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // FilterList
                filterByName(newText)

                return true
            }
        })


        // Navigation Button and Functions
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

    //Add new Record into Recommended RecyclerView
    private fun addDataToRecommendedList(){
        roomListRecommend.add(Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "Is a single room", 4, 2, 1500, "Male", 1))
        roomListRecommend.add(Rooms(2, "PV12", "Wangsa Maju, 50200 Kuala Lumpur", 540.50, "Is a single room", 3, 2, 1600, "Female", 2))
        roomListRecommend.add(Rooms(3, "PV15", "Wangsa Maju, 50200 Kuala Lumpur", 640.50, "Is a double room", 4, 2, 1700, "Male", 1))
        roomListRecommend.add(Rooms(4, "PV16", "Wangsa Maju, 50200 Kuala Lumpur", 740.50, "Is a double room", 5, 2, 1800, "Female", 2))
    }

    //Add new Record into Others RecyclerView
    private fun addDataToOthersList(){
        roomListOthers.add(Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "Is a single room", 4, 2, 1500, "Male", 1))
        roomListOthers.add(Rooms(2, "PV12", "Wangsa Maju, 50200 Kuala Lumpur", 540.50, "Is a single room", 3, 2, 1600, "Female", 2))
        roomListOthers.add(Rooms(3, "PV15", "Wangsa Maju, 50200 Kuala Lumpur", 640.50, "Is a double room", 4, 2, 1700, "Male", 1))
        roomListOthers.add(Rooms(4, "PV16", "Wangsa Maju, 50200 Kuala Lumpur", 740.50, "Is a double room", 5, 2, 1800, "Female", 2))
    }

    //Filter RecyclerView Record By Address
    private fun filterByAddress(newText : String){
        val filteredAddressList = ArrayList<Rooms>()
        for(i in roomListRecommend){
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

    //Filter RecyclerView Record By Name
    private fun filterByName(newText : String){
        if(newText != null){
            val filteredList = ArrayList<Rooms>()
            for(i in roomListOthers){
                if(i.name.lowercase(Locale.ROOT).contains(newText.lowercase())){
                    filteredList.add(i)
                }
            }
            if(filteredList.isEmpty()){
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
            }
            else{
                adaptor.setFilteredList(filteredList)
            }
        }
    }
}
