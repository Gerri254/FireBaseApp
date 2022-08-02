package com.gerald.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputName : EditText = findViewById(R.id.inputName)
        val inputEmail : EditText = findViewById(R.id.inputEmail)
        val inputAge : EditText = findViewById(R.id.inputAge)
        val buttonSave : Button =findViewById(R.id.buttonsave)
        val buttonFetch : Button =findViewById(R.id.buttonFetch)
        val txtRealTime : TextView =findViewById(R.id.txtRealTime)


        val database = Firebase.database
        val refusers = database.getReference("users")

        buttonSave.setOnClickListener {
            val name = inputName.text.toString().trim()
            val age = inputAge.text.toString().trim().toIntOrNull()
            val email = inputEmail.text.toString().trim()

            if(name.isNotEmpty()&& age != null && email.isNotEmpty()){
                //save
                val user = User(name,age,email)
                refusers.push().setValue(user)//saving,creates a new node and sets value on the new node
                inputAge.text.clear()
                inputEmail.text.clear()
                inputName.text.clear()

                val buttonDialog = BottomSheetDialog(this)
                buttonDialog.setContentView(R.layout.bottom_sheet)
                buttonDialog.show()
            }
        }


        // one-time fetch....
        buttonFetch.setOnClickListener {
            refusers.get().addOnSuccessListener {
               val intent = Intent(this,UsersActivity::class.java)
                startActivity(intent)
            }
        }

         val realtime = database.getReference("realtime")
        realtime.setValue("Walter")

        realtime.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              Log.d("UPDATING","$snapshot")
                txtRealTime.text = snapshot.getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }
}
