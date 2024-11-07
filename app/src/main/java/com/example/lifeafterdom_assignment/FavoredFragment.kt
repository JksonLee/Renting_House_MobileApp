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


class FavoredFragment : Fragment(), RoomsAdaptor.OnItemClickListener {
    // Database
    private lateinit var dbRef: DatabaseReference

    // Adaptor
    private lateinit var adaptor: RoomsAdaptor

    //Others
    private lateinit var recyclerViewFavored: RecyclerView
    private lateinit var roomListFavored: ArrayList<Rooms>
    private val args by navArgs<FavoredFragmentArgs>()
    private var userID by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favored, container, false)
        // ----------------------------------------------- Previous Argument ----------------------------------------------- //
        userID = args.userID


        // -------------------------------------------------- Navigation -------------------------------------------------- //
        val btnHome: Button = view.findViewById(R.id.btnHomePage)
        val btnFavored: Button = view.findViewById(R.id.btnFavored)
        val btnProfile: Button = view.findViewById(R.id.btnProfile)
        val btnProperty : Button = view.findViewById(R.id.btnProperty)

        btnProperty.setOnClickListener{
            val action = FavoredFragmentDirections.actionFavoredFragmentToPropertyPageFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Home Page
        btnHome.setOnClickListener {
            val action = FavoredFragmentDirections.actionFavoredFragmentToHomeFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Favored Page
        btnFavored.setOnClickListener {
            val action = FavoredFragmentDirections.actionFavoredFragmentSelf(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Profile Page
        btnProfile.setOnClickListener {
            val action =
                FavoredFragmentDirections.actionFavoredFragmentToProfilePageFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }


        // ------------------------------------------------- Retrieve Data ------------------------------------------------- //
        // Display Favored RecycleView
        recyclerViewFavored = view.findViewById(R.id.rvFRoom)
        recyclerViewFavored.layoutManager = LinearLayoutManager(context)
        recyclerViewFavored.setHasFixedSize(true)
        roomListFavored = arrayListOf()

        // Fetch Data Into RecyclerView
        fetchDataToFavoredList()

        return view
    }


    // ====================================================== Function ====================================================== //
    // Retrieve Data From Database for Favored RecycleView
    private fun fetchDataToFavoredList() {
        dbRef = FirebaseDatabase.getInstance().getReference("Favoreds")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomListFavored.clear()
                if (snapshot.exists()) {
                    for (roomsSnap in snapshot.children) {
                        if (roomsSnap.child("userID").value.toString().toInt() == userID) {
                            val roomID = roomsSnap.child("roomID").value.toString().toInt()
                            dbRef = FirebaseDatabase.getInstance().getReference("Rooms")
                            dbRef.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (rooms in snapshot.children) {
                                            if (rooms.child("roomID").value.toString().toInt() == roomID) {
                                                roomListFavored.add(
                                                    Rooms(rooms.child("roomID").value.toString().toInt(),
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
                                        adaptor = RoomsAdaptor(roomListFavored, this@FavoredFragment)
                                        recyclerViewFavored.adapter = adaptor
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Database No Room Record",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG)
                                        .show()
                                }
                            })
                        }
                    }
                } else {
                    Toast.makeText(context, "Database No Favored Record", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Onclick RecycleView Item and Navigation
    override fun itemClick(position: Int) {
        val roomSelectedList = roomListFavored[position]

        val action = FavoredFragmentDirections.actionFavoredFragmentToRoomDetailFragment(
            roomSelectedList.roomID,
            userID
        )

        findNavController().navigate(action)
    }
}