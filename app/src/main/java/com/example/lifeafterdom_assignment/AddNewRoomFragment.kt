package com.example.lifeafterdom_assignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.lifeafterdom_assignment.R
import com.example.lifeafterdom_assignment.data.Rooms
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import java.util.UUID
import kotlin.properties.Delegates

class AddNewRoomFragment : Fragment() {

    private lateinit var storageRef : StorageReference
    private val PICK_IMAGE_REQUEST = 1
    // Others
    private val args by navArgs<AddNewRoomFragmentArgs>()
    private var userID by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_add_new_room, container, false)
        storageRef = FirebaseStorage.getInstance().reference
        userID  = args.userID

        val userImage : ImageView = view.findViewById(R.id.UserPropertyImg)
        val typeSpinner : Spinner = view.findViewById(R.id.RoomTypeSpinner)
        val etRoomName: EditText = view.findViewById(R.id.etRoomName)
        val etRoomDescription: EditText = view.findViewById(R.id.etDescription)
        val etRoomAddress: EditText = view.findViewById(R.id.etRoomAddress)
        val spinnerGender: Spinner = view.findViewById(R.id.tvPreferGender)
        val etPrice: EditText = view.findViewById(R.id.etRoomPrice)
        val btnAddRoom : Button = view.findViewById(R.id.CreateNewRoomBtn)
        val btnCancel : Button = view.findViewById(R.id.CancelNewRoomBtn)

        btnCancel.setOnClickListener{
            val action = AddNewRoomFragmentDirections.actionAddNewRoomFragmentToPropertyPageFragment(userID)

            Navigation.findNavController(requireView()).navigate(action)
        }

        userImage.setOnClickListener{
            Log.e("TEST","TEST")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnAddRoom.setOnClickListener {
            val type = typeSpinner.selectedItem.toString()
            val roomName = etRoomName.text.toString()
            val description = etRoomDescription.text.toString()
            val roomAddress = etRoomAddress.text.toString()
            val price = etPrice.text.toString().toDouble()
            val roommate: String = spinnerGender.selectedItem.toString()

            val database = FirebaseDatabase.getInstance().getReference("Rooms")
            database.get().addOnSuccessListener { snapshot ->
                val lastRoomID = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.maxOrNull() ?: 0
                val newRoomID = lastRoomID + 1

                val agentID = userID

                val room = Rooms(newRoomID, roomName, roomAddress, price, type, description, roommate, agentID)

                database.child(newRoomID.toString()).setValue(room).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Room added successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        Navigation.findNavController(requireView()).popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to add room. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get the last room ID. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data ?: return
            val userImage: ImageView = view?.findViewById(R.id.UserPropertyImg) ?: return
            userImage.setImageURI(imageUri) // Set the selected image URI to the ImageView
            uploadImageToFirebase(imageUri)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("images")

        // List all items (images) in the folder
        storageReference.listAll()
            .addOnSuccessListener { listResult: ListResult ->
                val itemCount = listResult.items.size
                val newCount = itemCount + 1
                val filename = "room$newCount.png"
                val imageRef = storageReference.child(filename)

                imageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Use the download URL to set the image in the ImageView
                            val userImage: ImageView = view?.findViewById(R.id.UserPropertyImg) ?: return@addOnSuccessListener
                            userImage.setImageURI(uri)
                            Toast.makeText(requireContext(), "Successful to upload.", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { exception ->
                            Log.e("TEST", "Failed to get download URL:", exception)
                            Toast.makeText(requireContext(), "Failed to get download URL.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("TEST", "Image upload failed:", exception)
                        Toast.makeText(requireContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("TEST", "Failed to list images:", exception)
                Toast.makeText(requireContext(), "Failed to list images.", Toast.LENGTH_SHORT).show()
            }
    }
}