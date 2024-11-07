package com.example.lifeafterdom_assignment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.example.lifeafterdom_assignment.data.Users
import com.google.firebase.database.*
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
        val SpinnerGender: Spinner = view.findViewById(R.id.GenderSpinner)
        val etDateOfBirth: TextView = view.findViewById(R.id.etDateOfBirth)
        val etAddress: EditText = view.findViewById(R.id.etRegisterAddress)
        val SpinnerCountry: Spinner = view.findViewById(R.id.CountrySpinner)
        val SpinnerState: Spinner = view.findViewById(R.id.StateSpinner)
        val etPhoneNo: EditText = view.findViewById(R.id.etUserPhoneNo)
        val btnRegister: Button = view.findViewById(R.id.SignInBtn)
        val tvLoginAccount: TextView = view.findViewById(R.id.tvLoginAccount)
        val SpinnerCity: Spinner = view.findViewById(R.id.CitySpinner)
        val etZipCode: EditText = view.findViewById(R.id.etZipCode)

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
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
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
            val email = etUserEmail.text.toString().trim()
            val password = etUserPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val phoneNo = etPhoneNo.text.toString().trim()
            val date = etDateOfBirth.text.toString().trim()
            val address = etAddress.text.toString()
            val country: String = SpinnerCountry.selectedItem.toString()
            val state: String = SpinnerState.selectedItem.toString()
            val gender: String = SpinnerGender.selectedItem.toString()
            val city: String = SpinnerCity.selectedItem.toString()
            val zip: String = etZipCode.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNo.isEmpty() || date.isEmpty() || address.isEmpty() || country.isEmpty() || state.isEmpty() || gender.isEmpty() || city.isEmpty() || zip.isEmpty()) {
                if (name.isEmpty()) {
                    etName.error = "Name is required"
                }
                if (email.isEmpty()) {
                    etUserEmail.error = "Email is required"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(requireContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
                } else if (email[0].isDigit()) {
                    Toast.makeText(requireContext(), "Email address cannot start with a number.", Toast.LENGTH_SHORT).show()
                }
                if (password.isEmpty() || password.length < 6) {
                    Toast.makeText(requireContext(), "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                } else if (!password.matches(Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#\$%^&+=])(?=\\S+\$).{6,}\$"))) {
                    Toast.makeText(requireContext(), "Password must contain at least 1 uppercase letter, 1 number, and 1 special character.", Toast.LENGTH_SHORT).show()
                }
                if (password != confirmPassword) {
                    Toast.makeText(requireContext(), "Password and Retype Password do not match.", Toast.LENGTH_SHORT).show()
                }
                if (phoneNo.isEmpty()) {
                    etPhoneNo.error = "Phone number is required"
                }else if (!phoneNo.matches(Regex("^0[0-9]{10,11}$"))) {
                    etPhoneNo.error = "Phone number must start with 0 and have 10 or 11 digits."
                }

                if (date.isEmpty()) {
                    etDateOfBirth.error = "Date is required"
                }
                if (address.isEmpty()) {
                    etAddress.error = "Address is required"
                }
                if (zip.isEmpty()) {
                    etZipCode.error = "Zip Code is required"
                } else if (zip.length != 5) {
                    Toast.makeText(requireContext(), "The Zip Code should be 5 digits", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            val database = FirebaseDatabase.getInstance().getReference("Users")

            // Get the current highest user ID
            database.get().addOnSuccessListener { snapshot ->
                val lastUserID = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.maxOrNull() ?: 0
                val newUserID = lastUserID + 1

                // Create a Users object with the provided data
                val user = Users(newUserID, name, phoneNo, gender, address, country, state, city, zip, date, email, password)

                // Insert the user object into the Firebase database
                database.child(newUserID.toString()).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "User registered successfully!", Toast.LENGTH_SHORT).show()
                        // Navigate to the login fragment
                        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(requireContext(), "Failed to register user. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get the last user ID. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        tvLoginAccount.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }
}