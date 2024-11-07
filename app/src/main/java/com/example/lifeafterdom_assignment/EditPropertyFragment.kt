package com.example.lifeafterdom_assignment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.lifeafterdom_assignment.data.Rooms
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.DecimalFormat
import kotlin.properties.Delegates

class EditPropertyFragment : Fragment() {
    // Database
    private lateinit var dbRef: DatabaseReference
    private lateinit var imgRef : StorageReference
    // Format
    private var decimalFormat : DecimalFormat = DecimalFormat("RM ###,###.00")
    // Others
    private val args by navArgs<EditPropertyFragmentArgs>()
    private lateinit var filteredRoomDetailList : Rooms
    private lateinit var propertyList : ArrayList<Rooms>
    private var userID by Delegates.notNull<Int>()
    private var agentID by Delegates.notNull<Int>()
    private var selectedRoomID by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        selectedRoomID  = args.roomSelectedItem
        userID  = args.userID

        propertyList = arrayListOf()

        // Fetch Data
        return view
    }
}