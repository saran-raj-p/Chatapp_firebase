package com.example.itbatch2021

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.itbatch2021.databinding.ForgetpwdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Forgetpwd:Navigation() {
    lateinit var forgetpwdBinding: ForgetpwdBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgetpwdBinding = ForgetpwdBinding.inflate(layoutInflater)
        setContentView(forgetpwdBinding.root)
        val email:EditText = findViewById(R.id.rstpwd)

        auth = Firebase.auth
        val send:Button = findViewById(R.id.sendl)
        send.setOnClickListener{
            val setemail:String = email.text.toString()
            if(setemail.isEmpty()){
                email.setError("Empty")
                email.requestFocus()
            }
            else {
                Sendl(setemail)
            }
        }

    }
    private fun Sendl(email:String){
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task -> if(task.isSuccessful){
        Toast.makeText(this,"Sent",Toast.LENGTH_SHORT).show()}
            else{
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
        }
        }
    }
}