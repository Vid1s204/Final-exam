package com.example.groupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groupproject.QuizListAdapter
import com.example.groupproject.QuizModel
import com.example.groupproject.R
import com.example.groupproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    // Declare variables
    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList: MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter

    // Override onCreate method to initialize the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(findViewById(com.google.android.material.R.id.open_search_view_toolbar))

        // Initializing MutableList to hold QuizModel objects
        quizModelList = mutableListOf()
        getDataFromFirebase()
    }

    // Override onCreateOptionsMenu method to inflate the menu layout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Override onOptionsItemSelected method to handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Function to set up RecyclerView with adapter
    private fun setupRecyclerView() {
        binding.progressBar.visibility = View.GONE
        // Initialize adapter with QuizModelList and set it to RecyclerView
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // Function to fetch data from Firebase
    private fun getDataFromFirebase() {
        binding.progressBar.visibility = View.VISIBLE
        // Fetch data from Firebase Realtime Database
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot ->

                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }

                setupRecyclerView()
            }
    }
}
