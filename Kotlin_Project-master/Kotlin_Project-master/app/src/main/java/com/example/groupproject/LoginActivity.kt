package com.example.groupproject


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    // Declaring variable

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var loginButton: Button
    private lateinit var registerText: TextView

    // Override the onCreate method to initialize the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Initializing variables
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        registerText = findViewById(R.id.textViewRegister)

        // Setting up click listener for the login button
        loginButton.setOnClickListener {

            // Retrieve email and password from EditText fields
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Checking if email and password fields are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {

                loginUser(email, password)
            } else {

                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the registration option
        registerText.setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Function to login user with provided email and password
    private fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Log.d("LoginActivity", "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
