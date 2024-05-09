package com.example.lifeafterdom_assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.lifeafterdom_assignment.R

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val btnLogin: Button = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener() {
            val action =
                com.example.lifeafterdom_assignment.LoginFragmentDirections.actionLoginFragmentToHomeFragment()

            Navigation.findNavController(view).navigate(action)
        }
        return view
    }
}
