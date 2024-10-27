package com.example.itbatch2021

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.itbatch2021.databinding.CreateaccBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Createacc:Navigation(), View.OnClickListener {
    lateinit var createaccBinding: CreateaccBinding
    private lateinit var logint:Intent
    private lateinit var  getemail: EditText
    private lateinit var getpass: EditText
    private lateinit var getname: EditText
    private lateinit var getphone: EditText
    private lateinit var getreg: EditText
    private lateinit var getstaff: CheckBox
    private lateinit var getstudent: CheckBox
    private var settype:String? = null
    private lateinit var create_but: Button
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.database
    private val tb = db.getReference("Users")
    private lateinit var usrn:User
    private lateinit var dashint:Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createaccBinding = CreateaccBinding.inflate(layoutInflater)
        setContentView(createaccBinding.root)
        setTitle("Create Account")
        auth = FirebaseAuth.getInstance()
        getemail = findViewById(R.id.email)
        getpass = findViewById(R.id.password)
        getname = findViewById(R.id.cname)
        getphone = findViewById(R.id.phone)
        getreg= findViewById(R.id.regno)
        getstaff = findViewById(R.id.staffc)
        getstudent = findViewById(R.id.studentc)

        create_but = findViewById(R.id.create)
        logint = Intent(this, Loginacc::class.java)
        dashint = Intent(this,Dashboardact::class.java)
        getstaff.setOnClickListener(this)
        getstudent.setOnClickListener(this)
        create_but.setOnClickListener(this)
    }

     fun createuser(name:String,email: String, password: String) {
         auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
             if (task.isSuccessful) {
                 val user = auth.currentUser
                 tb.child("Users-details").child(user!!.uid).setValue(usrn).addOnCompleteListener(this) { task ->
                     if (task.isSuccessful) {
                         val prof = userProfileChangeRequest { displayName = name }
                         auth.currentUser!!.updateProfile(prof)
                         Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show()
                         if(auth.currentUser!=null){
                             startActivity(dashint)
                         }
                     } else {
                         Toast.makeText(this, "Not", Toast.LENGTH_LONG).show()
                     }
                 }

                 Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
             }
             else{
                 Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
             }
         }
     }
    private fun setval(sname:String,semail:String,spass:String,sphone:String,sreg:String?){
       usrn = User(sname,semail,spass,sphone,sreg)
    }

    override fun onClick(v: View?) {

        if(v?.id == R.id.staffc ){
            if (getstudent.isChecked) {
                getstaff.isChecked = false
                getreg.visibility = View.VISIBLE
                settype = "Student"


            } else {
                getstudent.isChecked = false
                getreg.visibility = View.GONE
                settype = "Staff"
            }
        }
        if(v?.id==R.id.studentc){
            if (getstaff.isChecked) {
                getstudent.isChecked = false
                getreg.visibility = View.GONE
                settype = "Staff"

            } else {
                getstaff.isChecked = false
                getreg.visibility = View.VISIBLE
                settype = "Student"
            }
        }
        else{
            val setname: String = getname.text.toString()
            val setemail: String = getemail.text.toString()
            val setpass: String = getpass.text.toString()
            val setphone: String = getphone.text.toString()
            val setreg: String? = getreg.text.toString()
            if (setname.isEmpty()) {
                getname.setError("Empty")
                getname.requestFocus()
            }  else if (setphone.isEmpty()) {
                getphone.setError("Empty")
                getphone.requestFocus()
            }
            else if (setemail.isEmpty()) {
                getemail.setError("Empty")
                getemail.requestFocus()
            }else if (setpass.isEmpty() || setpass.length < 8) {
                getpass.setError("Empty or Password length is less than 8 characters")
                getpass.requestFocus()
            } else if (getstudent.isChecked && setreg.isNullOrEmpty()) {
                getreg.setError("Register Number is Required")
                getreg.requestFocus()
            } else {
                setval(setname,setemail,setpass,setphone,setreg)
                createuser(setname,setemail,setpass)
            }
       }
    }

}
