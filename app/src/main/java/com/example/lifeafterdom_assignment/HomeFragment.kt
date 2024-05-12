package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import kotlin.properties.Delegates

class HomeFragment : Fragment(), RoomsAdaptor.onItemClickListener {
    private lateinit var searchRoom : SearchView
    private lateinit var recyclerViewRecommend : RecyclerView
    private lateinit var recyclerViewOthers : RecyclerView
    private lateinit var adaptor : RoomsAdaptor
    private lateinit var roomListRecommend : ArrayList<Rooms>
    private lateinit var roomListOthers : ArrayList<Rooms>
    private lateinit var dbRef: DatabaseReference
    private val args by navArgs<HomeFragmentArgs>()
    private var userID by Delegates.notNull<Int>()
    private lateinit var userAddress : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Catch Data from Previous
        userID  = args.userID

        // Navigation Button and Functions
        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)

        btnHome.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentSelf(userID)

            Navigation.findNavController(view).navigate(action)
        }

        btnFavored.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToFavoredFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        btnProfile.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToProfilePageFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Filter Option
        val spGender : Spinner = view.findViewById(R.id.spGender)
        val spBudget : Spinner = view.findViewById(R.id.spBudget)

        // Filter By Roommate Gender
        spGender.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterBySpinner(spGender.selectedItem.toString(),spBudget.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context, "No Record", Toast.LENGTH_SHORT).show()
            }
        }

        // Filter By Budget
        spBudget.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterBySpinner(spGender.selectedItem.toString(),spBudget.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context, "No Record", Toast.LENGTH_SHORT).show()
            }
        }

        // Display Recycle View of Others (Using Another Method to do recycle view)
        recyclerViewRecommend = view.findViewById(R.id.rvHRecommend)

        recyclerViewRecommend.setLayoutManager(LinearLayoutManager(context))
        recyclerViewRecommend.setHasFixedSize(true)

        roomListRecommend = arrayListOf()

        // Add Record Into RecyclerView
        fetchDataFromDatabase()



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

        // CLear All Option
        val btnClear : Button = view.findViewById(R.id.btnClear)

        btnClear.setOnClickListener{

            adaptor = RoomsAdaptor(roomListOthers, this@HomeFragment)
            recyclerViewOthers.adapter = adaptor
        }

        return view
    }


    // ======================================================================================================================= //
    // Function
    // Filter By Address
    private fun fetchDataFromDatabase(){
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(roomsSnap in snapshot.children){
                        if(roomsSnap.child("userID").value.toString().toInt() == userID){
                            val city = roomsSnap.child("city").value.toString()
                            dbRef = FirebaseDatabase.getInstance().getReference("Rooms")
                            dbRef.addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    roomListRecommend.clear()
                                    if(snapshot.exists()) {
                                        for(roomsSnap in snapshot.children){
                                            if(roomsSnap.child("address").value.toString().lowercase(Locale.ROOT).contains(city.lowercase())){
                                                roomListRecommend.add(Rooms(roomsSnap.child("roomID").value.toString().toInt(),
                                                    roomsSnap.child("name").value.toString(),
                                                    roomsSnap.child("address").value.toString(),
                                                    roomsSnap.child("price").value.toString().toDouble(),
                                                    roomsSnap.child("type").value.toString(),
                                                    roomsSnap.child("description").value.toString(),
                                                    roomsSnap.child("roommate").value.toString(),
                                                    roomsSnap.child("agentID").value.toString().toInt()))
                                            }else{
                                                Toast.makeText(context, "No Record", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        // Update List
                                        adaptor = RoomsAdaptor(roomListRecommend, this@HomeFragment)
                                        recyclerViewRecommend.adapter = adaptor
                                    }else{
//                                        Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                                }
                            })
                        }else{
//                            Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                        }
                    }
                    fetchDataToOthersList()
                }else {
//                    Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Add new Record into Others RecyclerView
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
//                    Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Filter RecyclerView Record By Name
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

    // Filter By Gender and Budget
    private fun filterBySpinner(roommate : String, budget : String){
        if(roommate != null && budget != null){
            val gender : String
            val min : Double
            val max : Double
            if(roommate == "Male Roommate"){
                gender = "male"
            }else{
                gender = "female"
            }
            if(budget == "Less Than RM500"){
                min = 0.00
                max = 500.00
            }else if(budget == "RM500 to RM799"){
                min = 500.00
                max = 800.00
            }else if(budget == "RM800 to RM1000"){
                min = 800.00
                max = 1000.01
            }else {
                min = 1000.01
                max = 9999.99
            }
            val filteredList = ArrayList<Rooms>()
            for (i in roomListOthers) {
                if ((i.roommate.lowercase(Locale.ROOT).equals(gender.lowercase())) &&
                    (i.price >= min) && (i.price < max)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
                recyclerViewOthers.adapter = adaptor
            } else {
                adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
                recyclerViewOthers.adapter = adaptor
            }
        }
    }

    // Onclick RecycleView Item and Navigation
    override fun itemClick(position: Int) {
        val roomSelectedList = roomListOthers[position]

        val action = HomeFragmentDirections.actionHomeFragmentToRoomDetailFragment(roomSelectedList.roomID, userID)

        findNavController().navigate(action)
    }
}
