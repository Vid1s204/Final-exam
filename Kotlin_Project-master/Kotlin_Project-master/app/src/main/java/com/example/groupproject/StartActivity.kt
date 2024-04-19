package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Replace lb and rb with the actual IDs of your buttons
        val lb = findViewById<Button>(R.id.lb)
        val rb = findViewById<Button>(R.id.rb)

        // Set click listeners for the buttons
        lb.setOnClickListener {
            // Start LoginActivity
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        }

        rb.setOnClickListener {
            // Start RegisterActivity
            startActivity(Intent(this@StartActivity, RegisterActivity::class.java))
        }
    }
}
