package com.example.itbatch2021

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itbatch2021.databinding.ChatlayBinding
import com.example.itbatch2021.databinding.MsglayBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class Chat : Navigation(), View.OnClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var chatlayBinding: ChatlayBinding
    lateinit var typemsg: EditText
    lateinit var send: ImageButton
    lateinit var auth: FirebaseAuth
    var prev:Long = 0
    var getuser = Firebase.database.getReference("Users").child("Users-details")
    val db = Firebase.database.getReference("Messages")
    private val messageList: MutableList<Messages> = mutableListOf() // List to store chat messages
    var count:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatlayBinding = ChatlayBinding.inflate(layoutInflater)
        setContentView(chatlayBinding.root)
        auth = FirebaseAuth.getInstance()
        typemsg = findViewById(R.id.getmessage)
        send = findViewById(R.id.send)
        recyclerView = findViewById(R.id.recycle)
        send.setOnClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        if(auth.currentUser == null){
            Intent(this,Loginacc::class.java).also { startActivity(it) }
        }
        val msglisten = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) { // Clear the previous messages before updating
               if(dataSnapshot.exists()) {
                   count = dataSnapshot.childrenCount
                   for(i in prev until count) {
                       val message = Messages(
                           dataSnapshot.child(i.toString()).child("sender").value.toString(),
                           dataSnapshot.child(i.toString()).child("message").value.toString(),
                           dataSnapshot.child(i.toString()).child("time").value.toString()
                       )
                       messageList.add(message) // Add each chat message to the list
                       prev++
                   }
                   recyclerView.adapter = MsgAdapt(messageList)
                   recyclerView.scrollToPosition(messageList.size-1)
               }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

       db.addValueEventListener(msglisten)
    }

    override fun onDestroy() {
        super.onDestroy()
        messageList.clear()
    }

        override fun onClick(v: View) {
            if (v.id == R.id.send) {
                var getuser = getuser.child(auth.currentUser!!.uid).child("username").get()
                getuser.addOnSuccessListener {
                    if (it.exists()) {
                        val sendern = it.value.toString()
                        val messageText = typemsg.text.toString().trim()
                        if (messageText.isNotEmpty()) {
                            getval(sendern, messageText)
                        }
                    }
                }
            }
        }

        @SuppressLint("NewApi")
        private fun getval(sender: String, mess: String) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yy")
            val current = LocalDateTime.now().format(formatter)
            val message = Messages(sender, mess,current) // Create a new message object
            db.child(count.toString()).setValue(message).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    typemsg.text.clear()
                } else {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }
        }

}
