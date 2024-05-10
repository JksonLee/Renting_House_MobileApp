package com.example.lifeafterdom_assignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class RoomDetailFragment : Fragment() {
    private val args by navArgs<RoomDetailFragmentArgs>()
    private lateinit var dbRef: DatabaseReference
    private lateinit var filteredRoomDetailList : Rooms
    private lateinit var roomDetailList : ArrayList<Rooms>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_detail, container, false)

        // Back Navigation
        val ibtnBack : ImageButton = view.findViewById(R.id.ibtnBack)

        ibtnBack.setOnClickListener{
            val action = RoomDetailFragmentDirections.actionRoomDetailFragmentToHomeFragment()

            Navigation.findNavController(view).navigate(action)
        }


        // Get Data from Previous
        val selectedRoomID  = args.roomSelectedItem

        roomDetailList = arrayListOf()

        addRecord()

        for(i in roomDetailList){
            if(i.roomID.toString().contains(selectedRoomID.toString())){
                filteredRoomDetailList = i
            }
        }

        // Display Information
//        val imgRDImg : ImageView = view.findViewById(R.id.imgRDImg)
        val tvRDName : TextView = view.findViewById(R.id.tvRDName)
        val tvRDAddress : TextView = view.findViewById(R.id.tvRDAddress)
        val tvRDPrice : TextView = view.findViewById(R.id.tvRDPrice)
        val tvRDType : TextView = view.findViewById(R.id.tvRDType)
        val tvRDRoommate : TextView = view.findViewById(R.id.tvRDRoommate)
        val tvRDDiscription : TextView = view.findViewById(R.id.tvRDDiscription)


//        imgRDImg.setImageResource(selectedByIDRoom.image)
        tvRDName.text = filteredRoomDetailList.name
        tvRDAddress.text = filteredRoomDetailList.address
        tvRDPrice.text = "RM " + filteredRoomDetailList.price.toString()
        tvRDType.text = filteredRoomDetailList.type
        tvRDRoommate.text = filteredRoomDetailList.roommate
        tvRDDiscription.text = filteredRoomDetailList.description




        // Add To Favored
        val ibtnFavored : ImageButton = view.findViewById(R.id.ibtnFavored)

        ibtnFavored.setOnClickListener{
            addRoomToFavored(selectedRoomID)
        }


        // Link To Whatsapp
        val btnContact : Button = view.findViewById(R.id.btnContact)

        btnContact.setOnClickListener{
            val uri = Uri.parse("smsto" + "+601139359182")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.setPackage("com.whatsapp")
            if(intent.resolveActivity(view.context.packageManager)!= null){
                startActivity(intent)
            }else{
                Toast.makeText(view.context,"Package is not installed", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    private fun addRecord(){
        roomDetailList.add(Rooms(1, "PV10", "Setapak, 50200 Kuala Lumpur", 440.50, "single","Is a single room", "Male", 1))
        roomDetailList.add(Rooms(2, "PV12", "Wangsa Maju, 50200 Kuala Lumpur", 540.50, "single","Is a single room", "Female", 2))
        roomDetailList.add(Rooms(3, "PV15", "Wangsa Maju, 50200 Kuala Lumpur", 640.50, "double","Is a double room", "Male", 1))
        roomDetailList.add(Rooms(4, "PV16", "Wangsa Maju, 50200 Kuala Lumpur", 740.50, "double","Is a double room", "Female", 2))
    }

    // Insert Favored Record Into Databse
    private fun addRoomToFavored(roomID : Int){
        // Insert Record Into Favored Table
        dbRef = FirebaseDatabase.getInstance().getReference("Favoreds")
        val favored = Favoreds(1, roomID, 1)
        dbRef.child(favored.favoredID.toString()).setValue(favored)
            .addOnCompleteListener {
                Toast.makeText(context, "Data saved", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error ${it.toString()}", Toast.LENGTH_LONG).show()
            }

    }
}