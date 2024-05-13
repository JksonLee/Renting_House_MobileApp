package com.example.lifeafterdom_assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.lifeafterdom_assignment.data.Rooms
import com.example.lifeafterdom_assignment.dataAdaptor.RoomsAdaptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlin.properties.Delegates

class LoginFragment : Fragment() {
    // Database

    // Adaptor

    // Others
    private val args by navArgs<LoginFragmentArgs>()
    private var userID by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        // ----------------------------------------------- Previous Argument ----------------------------------------------- //
        userID  = args.userID

        val etEmail: EditText = view.findViewById(R.id.etLoginEmail)
        val etPassword: EditText = view.findViewById(R.id.etPassword)
        val btnLogin: Button = view.findViewById(R.id.LoginBtn)
        val tvRegister: TextView = view.findViewById(R.id.tvRegisterAcount)

        btnLogin.setOnClickListener {
            val email: String = etEmail.text.toString()
            val password: String = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in the email and password.", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(requireContext(), "Invalid email format.", Toast.LENGTH_SHORT).show()
            } else {
                // Authenticate user with Firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Authentication successful, navigate to home fragment
                            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(userID)
                            Navigation.findNavController(view).navigate(action)
                        } else {
                            // Authentication failed, display error message
                            Toast.makeText(requireContext(), "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        tvRegister.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return view
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}