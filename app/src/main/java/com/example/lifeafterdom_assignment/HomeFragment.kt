package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.data.Rooms
import com.example.lifeafterdom_assignment.dataAdaptor.RoomsAdaptor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.Locale

class HomeFragment : Fragment(), RoomsAdaptor.onItemClickListener {
    private lateinit var searchRoom : SearchView
    private lateinit var recyclerViewRecommend : RecyclerView
    private lateinit var recyclerViewOthers : RecyclerView
    private lateinit var adaptor : RoomsAdaptor
    private lateinit var roomListRecommend : ArrayList<Rooms>
    private lateinit var roomListOthers : ArrayList<Rooms>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

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

        // Filter Option
        val spGender : Spinner = view.findViewById(R.id.spGender)
        val spBudget : Spinner = view.findViewById(R.id.spBudget)




        // Recommended
        // Display Recycle View of Others (Using Another Method to do recycle view)
        recyclerViewRecommend = view.findViewById(R.id.rvHRecommend)

        recyclerViewRecommend.setLayoutManager(LinearLayoutManager(context))
        recyclerViewRecommend.setHasFixedSize(true)

        roomListRecommend = arrayListOf()

        // Add Record Into RecyclerView
        fetchDataToRecommendedList()



        // Others
        // Display Recycle View of Others
        recyclerViewOthers = view.findViewById(R.id.rvHRoom)
        searchRoom = view.findViewById(R.id.svRoom)

        recyclerViewOthers.setLayoutManager(LinearLayoutManager(context))
        recyclerViewOthers.setHasFixedSize(true)

        roomListOthers = arrayListOf()

        // Add Record Into RecyclerView
        fetchDataToOthersList()

        // Search Room Name
        searchRoom.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterByName(newText)
                return true
            }
        })

        return view
    }

    //Add new Record into Recommended RecyclerView
    private fun fetchDataToRecommendedList(){
        dbRef = FirebaseDatabase.getInstance().getReference("Rooms")
        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                roomListRecommend.clear()
                if(snapshot.exists()) {
                    for(roomsSnap in snapshot.children){
                        roomListRecommend.add(Rooms(roomsSnap.child("roomID").value.toString().toInt(),
                            roomsSnap.child("name").value.toString(),
                            roomsSnap.child("address").value.toString(),
                            roomsSnap.child("price").value.toString().toDouble(),
                            roomsSnap.child("type").value.toString(),
                            roomsSnap.child("description").value.toString(),
                            roomsSnap.child("roommate").value.toString(),
                            roomsSnap.child("agentID").value.toString().toInt()))
                    }
                    // Filter By User Address
                    filterByAddress("Wangsa Maju")

                    // Update List
//                    adaptor = RoomsAdaptor(roomListRecommend, this@HomeFragment)
//                    recyclerViewRecommend.adapter = adaptor
                }else {
                    Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    //Add new Record into Others RecyclerView
    private fun fetchDataToOthersList(){
        dbRef = FirebaseDatabase.getInstance().getReference("Rooms")
        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                roomListOthers.clear()
                if(snapshot.exists()) {
                    for(roomsSnap in snapshot.children){
                        roomListOthers.add(Rooms(roomsSnap.child("roomID").value.toString().toInt(),
                            roomsSnap.child("name").value.toString(),
                            roomsSnap.child("address").value.toString(),
                            roomsSnap.child("price").value.toString().toDouble(),
                            roomsSnap.child("type").value.toString(),
                            roomsSnap.child("description").value.toString(),
                            roomsSnap.child("roommate").value.toString(),
                            roomsSnap.child("agentID").value.toString().toInt()))
                    }
                    // Update List
                    adaptor = RoomsAdaptor(roomListOthers, this@HomeFragment)
                    recyclerViewOthers.adapter = adaptor
                }else {
                    Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
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
            adaptor = RoomsAdaptor(filteredAddressList, this@HomeFragment)
            recyclerViewRecommend.adapter = adaptor
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
                adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
                recyclerViewOthers.adapter = adaptor
            }
            else{
                adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
                recyclerViewOthers.adapter = adaptor
            }
        }
    }

    // Onclick RecycleView Item and Navigation
    override fun itemClick(position: Int) {
        val roomSelectedList = roomListOthers[position]

        val action = HomeFragmentDirections.actionHomeFragmentToRoomDetailFragment(roomSelectedList.roomID)

        findNavController().navigate(action)
    }
}
