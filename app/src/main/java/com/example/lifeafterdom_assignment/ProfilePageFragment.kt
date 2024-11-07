package com.example.lifeafterdom_assignment

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.google.firebase.database.*
import kotlin.properties.Delegates

class ProfilePageFragment : Fragment() {
    private val database = Firebase.database
    private val usersRef = database.getReference("Users")
    private val args by navArgs<ProfilePageFragmentArgs>()
    private var userID by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        userID  = args.userID

        val btnHome : Button = view.findViewById(R.id.btnHomePage)
        val btnFavored : Button = view.findViewById(R.id.btnFavored)
        val btnProfile : Button = view.findViewById(R.id.btnProfile)
        val btnProperty : Button = view.findViewById(R.id.btnProperty)

        btnProperty.setOnClickListener{
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentToPropertyPageFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Home Page
        btnHome.setOnClickListener{
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentToHomeFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Favored Page
        btnFavored.setOnClickListener{
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentToFavoredFragment(userID)

            Navigation.findNavController(view).navigate(action)
        }

        // Navigation : To Profile Page
        btnProfile.setOnClickListener{
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentSelf(userID)

            Navigation.findNavController(view).navigate(action)
        }

        fetchAndDisplayUserData(view)
        setupIndividualClickListeners(view)


        return view
    }

    private fun fetchAndDisplayUserData(view: View) {
        val userRef = usersRef.child(args.userID.toString())

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    view.findViewById<TextView>(R.id.tvUserName).text = snapshot.child("name").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserEmail).text = snapshot.child("email").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserGender).text = snapshot.child("gender").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserDateOfBirth).text = snapshot.child("dateOfBirth").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserAddress).text = snapshot.child("address").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserCountry).text = snapshot.child("country").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserState).text = snapshot.child("state").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserCity).text = snapshot.child("city").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserZipCode).text = snapshot.child("zipCode").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserPhoneNo).text = snapshot.child("phone").getValue(String::class.java)
                    view.findViewById<TextView>(R.id.tvUserPassword).text = snapshot.child("password").getValue(String::class.java)
                } else {
                    // Handle if user data doesn't exist
                    Log.e("Firebase", "User data not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Data fetching failed: ${error.message}")
                Toast.makeText(requireContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupIndividualClickListeners(view: View) {
        view.findViewById<TextView>(R.id.tvUserName)?.setOnClickListener {
            showEditDialog("name", InputType.TYPE_CLASS_TEXT, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserEmail)?.setOnClickListener {
            showEditDialog("email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserPassword)?.setOnClickListener {
            showEditDialog("password", InputType.TYPE_TEXT_VARIATION_PASSWORD, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserGender)?.setOnClickListener {
            showEditDialog("gender", InputType.TYPE_CLASS_TEXT, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserDateOfBirth)?.setOnClickListener {
            showEditDialog("dateOfBirth", InputType.TYPE_DATETIME_VARIATION_DATE, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserAddress)?.setOnClickListener {
            showEditDialog("address", InputType.TYPE_CLASS_TEXT, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserCountry)?.setOnClickListener {
            showEditDialog("country", InputType.TYPE_CLASS_TEXT, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserState)?.setOnClickListener {
            showEditDialog("state", InputType.TYPE_CLASS_TEXT, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserCity)?.setOnClickListener {
            showEditDialog("city", InputType.TYPE_CLASS_TEXT, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserZipCode)?.setOnClickListener {
            showEditDialog("zipCode", InputType.TYPE_CLASS_NUMBER, it as TextView)
        }
        view.findViewById<TextView>(R.id.tvUserPhoneNo)?.setOnClickListener {
            showEditDialog("phoneNo", InputType.TYPE_CLASS_PHONE, it as TextView)
        }
    }


    private fun showEditDialog(fieldName: String, inputType: Int, textViewToUpdate: TextView) {
        val userID=args.userID.toString()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit $fieldName")

        val input = EditText(requireContext())
        input.setText(textViewToUpdate.text)
        input.inputType = inputType

        builder.setView(input)
        builder.setPositiveButton("Save") { _, _ ->
            val newValue = input.text.toString()
            usersRef.child(userID.toString()).child(fieldName).setValue(newValue)
                .addOnSuccessListener {
                    Log.e("Firebase", "ENTER 123123")
                    textViewToUpdate.text = newValue
                    Toast.makeText(requireContext(), "$fieldName updated!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Error updating $fieldName: ${e.message}")
                    Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}
