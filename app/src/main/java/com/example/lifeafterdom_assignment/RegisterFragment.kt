package com.example.lifeafterdom_assignment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.lifeafterdom_assignment.R
import java.util.Calendar

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val etName: EditText = view.findViewById(R.id.etRegisterName)
        val etUserEmail: EditText = view.findViewById(R.id.etRegisterEmail)
        val etUserPassword: EditText = view.findViewById(R.id.etRegisterPassword)
        val etConfirmPassword: EditText = view.findViewById(R.id.etConfirmPassword)
        val GenderSpinner: Spinner = view.findViewById(R.id.GenderSpinner)
        val etDateOfBirth: TextView = view.findViewById(R.id.etDateOfBirth)
        val etAddress: EditText = view.findViewById(R.id.etRegisterAddress)
        val CountrySpinner: Spinner = view.findViewById(R.id.CountrySpinner)
        val StateSpinner: Spinner = view.findViewById(R.id.StateSpinner)
        val etPhoneNo: EditText = view.findViewById(R.id.etUserPhoneNo)
        val btnRegister: Button = view.findViewById(R.id.SignInBtn)
        val tvLoginAccount: TextView = view.findViewById(R.id.tvLoginAccount)

        // Set OnClickListener for the TextView
        etDateOfBirth.setOnClickListener {
            // Get current date to set as default in the DatePickerDialog
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a DatePickerDialog and set the initial date to the current date
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Update the TextView with the selected date
                    etDateOfBirth.text = getString(
                        R.string.date_format,
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etUserEmail.text.toString()
            val password = etUserPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val phoneNo = etPhoneNo.text.toString()
            val date = etDateOfBirth.text.toString()
            val address = etAddress.text.toString()
            val country : String = CountrySpinner.selectedItem.toString()
            val state : String = StateSpinner.selectedItem.toString()
            val gender : String = GenderSpinner.selectedItem.toString()

            // Validate for Username field
            if (name.isEmpty()) {
                etName.error = "Name is required"
                return@setOnClickListener
            }

            // Validate that all fields are filled
            if (email.isEmpty()) {
                etUserEmail.error = "Email is required"
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            }else if(email[0].isDigit()) {
                Toast.makeText(requireContext(), "Email address cannot start with a number.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate that the password is at least 6 characters long
            if (password.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#\$%^&+=])(?=\\S+\$).{6,}\$".toRegex()

            if (!passwordRegex.matches(password)) {
                Toast.makeText(requireContext(), "Password must contain at least 1 uppercase letter, 1 number, 1 '@' or '_', and must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate that the password and retype password fields match
            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Password and Retype Password do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // You can add more specific validation for phone number if needed
            if (phoneNo.isEmpty()) {
                etPhoneNo.error = "Phone number is required"
                return@setOnClickListener
            }

            // If all validations pass, proceed with your sign-in logic here
        }

        tvLoginAccount.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }
}