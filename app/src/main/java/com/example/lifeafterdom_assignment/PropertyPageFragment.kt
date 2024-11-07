package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
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
import kotlin.properties.Delegates

class PropertyPageFragment : Fragment(), RoomsAdaptor.OnItemClickListener {
    // Database
    private lateinit var dbRef: DatabaseReference

    // Adaptor
    private lateinit var adaptor: RoomsAdaptor

    //Others
    private lateinit var recyclerViewProperty: RecyclerView
    private lateinit var propertyList: ArrayList<Rooms>
    private val args by navArgs<PropertyPageFragmentArgs>()
    private var userID by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_property_page, container, false)

        userID  = args.userID

        // Fetch Data Into RecyclerView
        fetchUserPropertyData()

        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)
        val btnProperty : Button = view.findViewById(R.id.btnProperty)
        val btnAddNewRoom : ImageButton = view.findViewById(R.id.addNewRoomBtn)

        btnProperty.setOnClickListener{
            val action = PropertyPageFragmentDirections.actionPropertyPageFragmentSelf(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Home Page
        btnHome.setOnClickListener{
            val action = PropertyPageFragmentDirections.actionPropertyPageFragmentToHomeFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Favored Page
        btnFavored.setOnClickListener{
            val action = PropertyPageFragmentDirections.actionPropertyPageFragmentToFavoredFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Profile Page
        btnProfile.setOnClickListener{
            val action = PropertyPageFragmentDirections.actionPropertyPageFragmentToProfilePageFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        btnAddNewRoom.setOnClickListener{
            val action = PropertyPageFragmentDirections.actionPropertyPageFragmentToAddNewRoomFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }


        recyclerViewProperty = view.findViewById(R.id.rvUserProperty)
        recyclerViewProperty.layoutManager = LinearLayoutManager(context)
        recyclerViewProperty.setHasFixedSize(true)
        propertyList = arrayListOf()

        return view
    }

    private fun fetchUserPropertyData(){
        dbRef = FirebaseDatabase.getInstance().getReference("Rooms")
        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                propertyList.clear()
                if(snapshot.exists()) {
                    for(roomsSnap in snapshot.children){
                        if (roomsSnap.child("agentID").value.toString().toInt() == userID){
                            propertyList.add(Rooms(roomsSnap.child("roomID").value.toString().toInt(),
                                roomsSnap.child("name").value.toString(),
                                roomsSnap.child("address").value.toString(),
                                roomsSnap.child("price").value.toString().toDouble(),
                                roomsSnap.child("type").value.toString(),
                                roomsSnap.child("description").value.toString(),
                                roomsSnap.child("roommate").value.toString(),
                                roomsSnap.child("agentID").value.toString().toInt()))
                        }
                    }
                    // Update List
                    adaptor = RoomsAdaptor(propertyList, this@PropertyPageFragment)
                    recyclerViewProperty.adapter = adaptor
                }else {
                    Toast.makeText(context, "Database No Room Record", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun itemClick(position: Int) {
        val roomSelectedList = propertyList[position]

        val action = PropertyPageFragmentDirections.actionPropertyPageFragmentToEditPropertyFragment(roomSelectedList.roomID, userID)

        findNavController().navigate(action)
    }

}