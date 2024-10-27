package com.example.itbatch2021

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material3.TopAppBar
import com.example.itbatch2021.databinding.LoginActBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Loginacc : Navigation(), View.OnClickListener {
    private lateinit var loginActBinding: LoginActBinding
    private lateinit var getpass:EditText
    private lateinit var getlemail:EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var creint:Intent
    private lateinit var forgetint:Intent
    private lateinit var dashint:Intent
    private lateinit var frgtv:TextView
    private lateinit var log_but:Button
    private lateinit var button_create:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActBinding = LoginActBinding.inflate(layoutInflater)
        setContentView(loginActBinding.root)
        setTitle("Login")
        auth = Firebase.auth
        getlemail = findViewById(R.id.userl)
        getpass = findViewById(R.id.passwordl)
        creint = Intent(this,Createacc::class.java)
        forgetint = Intent(this,Forgetpwd::class.java)
        dashint = Intent(this,Dashboardact::class.java)
        frgtv= findViewById(R.id.forget)
        log_but = findViewById(R.id.logbutton)
        button_create = findViewById(R.id.createb)
        log_but.setOnClickListener(this)
        frgtv.setOnClickListener(this)
        button_create.setOnClickListener(this)
        if (auth.currentUser!=null){
            finish()
            startActivity(dashint)
        }
    }

    private fun Loginusr(email:String,password:String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task -> if(task.isSuccessful){
            val user = auth.currentUser
            Toast.makeText(this,"Sucess",Toast.LENGTH_LONG).show()
            startActivity(dashint)
        }
        else{
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
        }
        }
    }

    override fun onClick(v: View?) {
        if(v?.id==R.id.logbutton){
            val setlemail:String = getlemail.text.toString()
            val setlpass:String = getpass.text.toString()
            if (setlemail.isEmpty()) {
                getlemail.setError("Empty")
                getlemail.requestFocus()
            }
            else if (setlpass.isEmpty()) {
                getpass.setError("Empty")
                getpass.requestFocus()
            }
            else{
                Loginusr(setlemail,setlpass)
            }
        }
        else if(v?.id == R.id.forget){
            startActivity(forgetint)
        }
        else{
            startActivity(creint)
        }
    }
}