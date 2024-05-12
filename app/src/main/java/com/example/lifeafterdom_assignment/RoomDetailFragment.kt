package com.example.lifeafterdom_assignment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.lifeafterdom_assignment.data.Favoreds
import com.example.lifeafterdom_assignment.data.Rooms
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale
import kotlin.properties.Delegates

class RoomDetailFragment : Fragment() {
    private val args by navArgs<RoomDetailFragmentArgs>()
    private lateinit var dbRef: DatabaseReference
    private lateinit var filteredRoomDetailList : Rooms
    private lateinit var roomDetailList : ArrayList<Rooms>
    private var userID by Delegates.notNull<Int>()
    private var selectedRoomID by Delegates.notNull<Int>()
    private var totalRecord : Int = 0
    private lateinit var ibtnFavored : ImageButton
    private var favoredExistsID : Int = 0
    private var agentID by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_detail, container, false)

        // Catch Data from Previous
        selectedRoomID  = args.roomSelectedItem
        userID  = args.userID

        // Change Button
        checkFavoredButton(selectedRoomID)

        // Back Navigation
        val ibtnBack : ImageButton = view.findViewById(R.id.ibtnBack)

        ibtnBack.setOnClickListener{
            val action = RoomDetailFragmentDirections.actionRoomDetailFragmentToHomeFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Fetch Data From Database
        roomDetailList = arrayListOf()

        addRecord(selectedRoomID)


        // Add To Favored
        ibtnFavored = view.findViewById(R.id.ibtnFavored)

        ibtnFavored.setOnClickListener{
            addRoomToFavored(selectedRoomID)
        }


        // Message To Agent
        val btnContact : Button = view.findViewById(R.id.btnContact)

        btnContact.setOnClickListener{
            retrieveAgentPhoneNumberByID(agentID)
        }

        return view
    }


    // ======================================================================================================================= //
    // Function
    // Retrieve Data From Database
    private fun addRecord(selectedRoomID : Int){
        dbRef = FirebaseDatabase.getInstance().getReference("Rooms")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomDetailList.clear()
                if(snapshot.exists()) {
                    for(roomsSnap in snapshot.children){
                        if(roomsSnap.child("roomID").value.toString().toInt() == selectedRoomID){
                            agentID = roomsSnap.child("agentID").value.toString().toInt()
                            roomDetailList.add(Rooms(roomsSnap.child("roomID").value.toString().toInt(),
                                roomsSnap.child("name").value.toString(),
                                roomsSnap.child("address").value.toString(),
                                roomsSnap.child("price").value.toString().toDouble(),
                                roomsSnap.child("type").value.toString(),
                                roomsSnap.child("description").value.toString(),
                                roomsSnap.child("roommate").value.toString(),
                                roomsSnap.child("agentID").value.toString().toInt()))
                        }else{
                            Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Update List
                    for(i in roomDetailList){
                        if(i.roomID.toString().contains(selectedRoomID.toString())) {
                            filteredRoomDetailList = i
                        }
                    }

                    // Display Information
//                    val imgRDImg : ImageView = view.findViewById(R.id.imgRDImg)
                    val tvRDName : TextView = view!!.findViewById(R.id.tvRDName)
                    val tvRDAddress : TextView = view!!.findViewById(R.id.tvRDAddress)
                    val tvRDPrice : TextView = view!!.findViewById(R.id.tvRDPrice)
                    val tvRDType : TextView = view!!.findViewById(R.id.tvRDType)
                    val tvRDRoommate : TextView = view!!.findViewById(R.id.tvRDRoommate)
                    val tvRDDiscription : TextView = view!!.findViewById(R.id.tvRDDiscription)

//                    imgRDImg.setImageResource(selectedByIDRoom.image)
                    tvRDName.text = filteredRoomDetailList.name
                    tvRDAddress.text = filteredRoomDetailList.address
                    tvRDPrice.text = "RM " + filteredRoomDetailList.price.toString()
                    tvRDType.text = filteredRoomDetailList.type
                    tvRDRoommate.text = filteredRoomDetailList.roommate
                    tvRDDiscription.text = filteredRoomDetailList.description
                }else {
//                    Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })}

    // Insert Favored Record Into Database
    private fun addRoomToFavored(roomID : Int){
        // Check Record Into Favored Table
        if(favoredExistsID > 0){
            ibtnFavored.setImageResource(R.drawable.unfavored)
            dbRef = FirebaseDatabase.getInstance().getReference("Favoreds")
            dbRef.child(favoredExistsID.toString()).removeValue()
                .addOnCompleteListener{
                    ibtnFavored.setImageResource(R.drawable.unfavored)
                    Toast.makeText(context, "Remove From Favored Successful", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{
                    Toast.makeText(context, "Error $it", Toast.LENGTH_LONG).show()
                }
        }else{
            // Insert Record
            val favored = Favoreds((totalRecord+1), roomID, userID)
            dbRef = FirebaseDatabase.getInstance().getReference("Favoreds")
            dbRef.child(favored.favoredID.toString()).setValue(favored)
                .addOnCompleteListener {
                    ibtnFavored.setImageResource(R.drawable.favored)
                    Toast.makeText(context, "Add To Favored Successful", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error $it", Toast.LENGTH_LONG).show()
                }
        }
    }

    // Check Favored Button
    private fun checkFavoredButton(roomID : Int){
        // Check Record Into Favored Table
        dbRef = FirebaseDatabase.getInstance().getReference("Favoreds")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                totalRecord = 0
                favoredExistsID = 0
                if(snapshot.exists()) {
                    for(roomsSnap in snapshot.children){
                        totalRecord = roomsSnap.child("favoredID").value.toString().toInt()
                        if((roomsSnap.child("roomID").value.toString().toInt() == roomID)&&
                            (roomsSnap.child("userID").value.toString().toInt() == userID)){
                            favoredExistsID = roomsSnap.child("favoredID").value.toString().toInt()
                        }
                    }
                    if(favoredExistsID > 0){
                        ibtnFavored.setImageResource(R.drawable.favored)
                    }else{
                        ibtnFavored.setImageResource(R.drawable.unfavored)
                    }
                }else {
                    ibtnFavored.setImageResource(R.drawable.unfavored)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Retrieve Agent Phone Number
    private fun retrieveAgentPhoneNumberByID(agentID : Int){
        dbRef = FirebaseDatabase.getInstance().getReference("Agents")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var phoneNumber = ""
                    for(roomsSnap in snapshot.children){
                        if(roomsSnap.child("agentID").value.toString() == agentID.toString()){
                            phoneNumber = roomsSnap.child("phone").value.toString()

                        }else{
                            Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                        }
                    }

                    val url = Uri.parse("sms:$phoneNumber")
                    val intent = Intent(Intent.ACTION_VIEW, url)
                    startActivity(intent)
                }else {
//                    Toast.makeText(context, "No Data Found From Database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }
}