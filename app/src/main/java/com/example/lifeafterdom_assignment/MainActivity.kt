package com.example.lifeafterdom_assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Auto Inset Data for testing purpose
//        dbRef = FirebaseDatabase.getInstance().getReference("Agents")
//        val agent = Agents(1, "John", "0123456798", "Male")
//        dbRef.child(agent.agentID.toString()).setValue(agent)
//            .addOnCompleteListener {
//                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Error ${it.toString()}", Toast.LENGTH_LONG).show()
//            }
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Favoreds")
//        val favored = Favoreds(0, 0, 0)
//        dbRef.child(favored.favoredID.toString()).setValue(favored)
//            .addOnCompleteListener {
//                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Error ${it.toString()}", Toast.LENGTH_LONG).show()
//            }
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Rooms")
//        val room = Rooms(2, "PS10", "Wangsa Maju, 50200 Kuala Lumpur", 540.50, "single","Is a single room", "Female", 1)
//        dbRef.child(room.roomID.toString()).setValue(room)
//            .addOnCompleteListener {
//                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Error ${it.toString()}", Toast.LENGTH_LONG).show()
//            }
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Users")
//        val user = Users(1, "JJ", "0101234567", "Male", "Setapak,Kuala Lumpur", "Setapak")
//        dbRef.child(user.userID.toString()).setValue(user)
//            .addOnCompleteListener {
//                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Error ${it.toString()}", Toast.LENGTH_LONG).show()
//            }
    }
}