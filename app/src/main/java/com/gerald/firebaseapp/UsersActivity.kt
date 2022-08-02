package com.gerald.firebaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        val recyclerUsers : RecyclerView =findViewById(R.id.recyclerUsers)

        val database = Firebase.database
        val usersRef = database.getReference("users")
        val usersList = ArrayList<User>()

        val layoutManager = LinearLayoutManager(this)
        recyclerUsers.layoutManager = layoutManager

        val divider = DividerItemDecoration(this,layoutManager.orientation)
        recyclerUsers.addItemDecoration(divider)//lines....afert

        val adapter =CustomAdapter(usersList)
        recyclerUsers.adapter = adapter

        //listener to usersRef to fetch data in realTime
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for(child in snapshot.children){
                    val person = child.getValue(User::class.java)
                    usersList.add(person!!)

                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}