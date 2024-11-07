package com.example.lifeafterdom_assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {
    private lateinit var dbRef: DatabaseReference
    private var userID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val etEmail: EditText = view.findViewById(R.id.etLoginEmail)
        val etPassword: EditText = view.findViewById(R.id.etPassword)
        val btnLogin: Button = view.findViewById(R.id.LoginBtn)
        val tvRegister: TextView = view.findViewById(R.id.tvRegisterAcount)

        btnLogin.setOnClickListener {
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in the email and password.", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(requireContext(), "Invalid email format.", Toast.LENGTH_SHORT).show()
            } else {
                checking(email, password)
            }
        }

        tvRegister.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }

    private fun checking(email: String, password: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        if ((userSnap.child("email").value.toString() == email) &&
                            (userSnap.child("password").value.toString() == password)) {
                            userID = userSnap.child("userID").value.toString().toInt()
                            break
                        }
                    }
                    // Check and Navigation
                    if (userID > 0) {
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(userID)
                        if (findNavController().currentDestination?.id == R.id.loginFragment) {
                            findNavController().navigate(action)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Email or Password is wrong, please try again", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Database No Room Record", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}