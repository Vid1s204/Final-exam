package com.example.groupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.groupproject.databinding.ActivityRegisterBinding
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adding a log statement to check if onCreate() is executed
        Log.d("RegisterActivity", "onCreate() executed")

        binding.button.setOnClickListener {
            // Adding a log statement to check if the button click listener is triggered
            Log.d("RegisterActivity", "Register button clicked")

            val studentName = binding.SName.text.toString()
            val studentEmail = binding.SEmail.text.toString()
            val studentPassword = binding.SPassword.text.toString()

            // Check if the email already exists
            checkIfEmailExists(studentEmail, studentName, studentPassword)
        }

        // Adding onClickListener for the "Click here to login" TextView
        binding.textLogin.setOnClickListener {
            // Redirect to login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun checkIfEmailExists(email: String, name: String, password: String) {
        database = FirebaseDatabase.getInstance().getReference("User")
        val query: Query = database.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email already exists, display message
                    Toast.makeText(this@RegisterActivity, "Email already exists. Please login.", Toast.LENGTH_SHORT).show()
                } else {
                    // Email doesn't exist, proceed with registration
                    saveUserData(name, email, password)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e("RegisterActivity", "Database error: ${databaseError.message}")
                Toast.makeText(this@RegisterActivity, "Database error. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData(name: String, email: String, password: String) {
        // Save user data to Firebase
        database = FirebaseDatabase.getInstance().getReference("User")

        val user1 = User(name, email, password)

        database.child(name).setValue(user1)
            .addOnSuccessListener {
                // Clearing the input fields after successful data saving
                binding.SName.text.clear()
                binding.SEmail.text.clear()
                binding.SPassword.text.clear()
                // Show success message
                Toast.makeText(this, "Data Saved Successfully", Toast.LENGTH_SHORT).show()
                // Redirect to login activity
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Close current activity
            }
            .addOnFailureListener { exception ->
                // Show failure message
                Toast.makeText(this, "Operation Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                // Adding a log statement to log the error message
                Log.e("RegisterActivity", "Operation Failed", exception)
            }
    }
}
