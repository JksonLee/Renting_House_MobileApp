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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale


class FavoredFragment : Fragment(), RoomsAdaptor.onItemClickListener {
    private lateinit var recyclerViewFavored : RecyclerView
    private lateinit var adaptor : RoomsAdaptor
    private lateinit var roomListFavored : ArrayList<Rooms>
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favored, container, false)


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


        // Favored
        // Display User Favored List
        recyclerViewFavored = view.findViewById(R.id.rvFRoom)

        recyclerViewFavored.setLayoutManager(LinearLayoutManager(view.getContext()))
        recyclerViewFavored.setHasFixedSize(true)

        roomListFavored = arrayListOf()

//        adaptor = RoomsAdaptor(roomListFavored,this)
//        recyclerViewFavored.adapter = adaptor

        // Add Record Into RecyclerView
        fetchDataToFavoredList()

        return view
    }

    //Add new Record into Others RecyclerView
    private fun fetchDataToFavoredList(){
        dbRef = FirebaseDatabase.getInstance().getReference("Favoreds")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomListFavored.clear()
                if(snapshot.exists()) {
                    for(roomsSnap in snapshot.children){
                        roomListFavored.add(Rooms(roomsSnap.child("roomID").value.toString().toInt(),
                            roomsSnap.child("name").value.toString(),
                            roomsSnap.child("address").value.toString(),
                            roomsSnap.child("price").value.toString().toDouble(),
                            roomsSnap.child("type").value.toString(),
                            roomsSnap.child("description").value.toString(),
                            roomsSnap.child("roommate").value.toString(),
                            roomsSnap.child("agentID").value.toString().toInt()))
                    }
                    // Filter By User ID


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

    override fun itemClick(position: Int) {
        val roomSelectedList = roomListFavored[position]

        val action = FavoredFragmentDirections.actionFavoredFragmentToRoomDetailFragment(roomSelectedList.roomID)

        findNavController().navigate(action)
    }
}