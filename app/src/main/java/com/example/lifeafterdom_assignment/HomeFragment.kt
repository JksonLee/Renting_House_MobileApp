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
import java.util.Locale
import kotlin.properties.Delegates

class HomeFragment : Fragment(), RoomsAdaptor.OnItemClickListener {
    // Database
    private lateinit var dbRef: DatabaseReference
    // Adaptor
    private lateinit var adaptor : RoomsAdaptor
    // Others
    private lateinit var recyclerViewRecommend : RecyclerView
    private lateinit var recyclerViewOthers : RecyclerView
    private lateinit var roomListRecommend : ArrayList<Rooms>
    private lateinit var roomListOthers : ArrayList<Rooms>
    private lateinit var searchRoom : SearchView
    private val args by navArgs<HomeFragmentArgs>()
    private var userID by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // ----------------------------------------------- Previous Argument ----------------------------------------------- //
        userID  = args.userID


        // -------------------------------------------------- Navigation -------------------------------------------------- //
        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)

        // Navigation : To Home Page
        btnHome.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentSelf(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Favored Page
        btnFavored.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToFavoredFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Profile Page
        btnProfile.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToProfilePageFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }


        // ------------------------------------------------- Retrieve Data ------------------------------------------------- //
        // Display Recommended RecycleView
        recyclerViewRecommend = view.findViewById(R.id.rvHRecommend)
        recyclerViewRecommend.layoutManager = LinearLayoutManager(context)
        recyclerViewRecommend.setHasFixedSize(true)
        roomListRecommend = arrayListOf()

        // Fetch Data Into RecyclerView
        fetchDataFromDatabase()

        // Display Others RecycleView
        recyclerViewOthers = view.findViewById(R.id.rvHRoom)
        searchRoom = view.findViewById(R.id.svRoom)
        recyclerViewOthers.layoutManager = LinearLayoutManager(context)
        recyclerViewOthers.setHasFixedSize(true)
        roomListOthers = arrayListOf()

        // Fetch Data Into RecyclerView
        fetchDataToOthersList()


        // ------------------------------------------------- Function Part ------------------------------------------------- //
        val spGender : Spinner = view.findViewById(R.id.spGender)
        val spBudget : Spinner = view.findViewById(R.id.spBudget)

        // Function : Spinner Item Change
        spGender.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterBySpinner(spGender.selectedItem.toString(),spBudget.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Function : Spinner Item Change
        spBudget.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterBySpinner(spGender.selectedItem.toString(),spBudget.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Function : Search Room Name
        searchRoom.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterByName(newText)
                return true
            }
        })

        // Function : Clear All Filter Option
        val btnClear : Button = view.findViewById(R.id.btnClear)

        btnClear.setOnClickListener{

            adaptor = RoomsAdaptor(roomListOthers, this@HomeFragment)
            recyclerViewOthers.adapter = adaptor
        }

        return view
    }


    // ====================================================== Function ====================================================== //
    // Retrieve Data From Database for Recommended RecycleView
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
                                        for(rooms in snapshot.children){
                                            if(rooms.child("address").value.toString().lowercase(Locale.ROOT).contains(city.lowercase())){
                                                roomListRecommend.add(Rooms(rooms.child("roomID").value.toString().toInt(),
                                                    rooms.child("name").value.toString(),
                                                    rooms.child("address").value.toString(),
                                                    rooms.child("price").value.toString().toDouble(),
                                                    rooms.child("type").value.toString(),
                                                    rooms.child("description").value.toString(),
                                                    rooms.child("roommate").value.toString(),
                                                    rooms.child("agentID").value.toString().toInt()))
                                            }
                                        }
                                        // Update List
                                        adaptor = RoomsAdaptor(roomListRecommend, this@HomeFragment)
                                        recyclerViewRecommend.adapter = adaptor
                                    }else{
                                        Toast.makeText(context, "Database No Room Record", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    fetchDataToOthersList()
                }else {
                    Toast.makeText(context, "Database No User Record", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Retrieve Data From Database for Others RecycleView
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
                    Toast.makeText(context, "Database No Room Record", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Filter Data By Name
    private fun filterByName(newText : String){
        val filteredList = ArrayList<Rooms>()
        for(i in roomListOthers){
            if(i.name.lowercase(Locale.ROOT).contains(newText.lowercase())){
                filteredList.add(i)
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(context, "No Similar Room Name Found", Toast.LENGTH_SHORT).show()
            adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
            recyclerViewOthers.adapter = adaptor
        }
        else{
            adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
            recyclerViewOthers.adapter = adaptor
        }
    }

    // Filter Data By Gender and Budget
    private fun filterBySpinner(roommate : String, budget : String){
        val min : Double
        val max : Double
        val gender : String = if(roommate == "Male Roommate"){
            "male"
        }else{
            "female"
        }
        when (budget) {
            "Less Than RM500" -> {
                min = 0.00
                max = 500.00
            }
            "RM500 to RM799" -> {
                min = 500.00
                max = 800.00
            }
            "RM800 to RM1000" -> {
                min = 800.00
                max = 1000.01
            }
            else -> {
                min = 1000.01
                max = 9999.99
            }
        }
        val filteredList = ArrayList<Rooms>()
        for (i in roomListOthers) {
            if ((i.roommate.lowercase(Locale.ROOT) == gender.lowercase()) &&
                (i.price >= min) && (i.price < max)) {
                filteredList.add(i)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(context, "No Similar Requirement Room Found", Toast.LENGTH_SHORT).show()
            adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
            recyclerViewOthers.adapter = adaptor
        } else {
            adaptor = RoomsAdaptor(filteredList, this@HomeFragment)
            recyclerViewOthers.adapter = adaptor
        }
    }

    // Onclick RecycleView Item and Navigation
    override fun itemClick(position: Int) {
        val roomSelectedList = roomListOthers[position]

        val action = HomeFragmentDirections.actionHomeFragmentToRoomDetailFragment(roomSelectedList.roomID, userID)

        findNavController().navigate(action)
    }
}
