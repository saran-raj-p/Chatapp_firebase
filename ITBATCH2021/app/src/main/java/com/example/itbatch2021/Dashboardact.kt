package com.example.itbatch2021

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract.Contacts.Photo
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.itbatch2021.databinding.DashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.values
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URL

class Dashboardact:Navigation(), View.OnClickListener {
    lateinit var dashboardBinding: DashboardBinding
    lateinit var auth: FirebaseAuth
    val db = Firebase.database.getReference("Users").child("Users-details")
    public lateinit var sharen:String
    lateinit var getprof:ActivityResultLauncher<String>
    lateinit var logint:Intent
    lateinit var reset:Button
    lateinit var signout:Button
    lateinit var update:Button
    lateinit var picup : ImageButton
    lateinit var img:CircleImageView
    lateinit var dbname:TextView
    lateinit var dbemail:TextView
    lateinit var dbphone:TextView
  var storage = Firebase.storage.reference
    var uri:Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = DashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)
        auth = Firebase.auth
        logint = Intent(this,Loginacc::class.java)
        reset = findViewById(R.id.resetpwd)
        signout = findViewById(R.id.signout)
        update = findViewById(R.id.updateprof)
        dbname = findViewById(R.id.dbname)
        dbemail = findViewById(R.id.dbemail)
        dbphone = findViewById(R.id.dbphone)
        picup = findViewById(R.id.picu)
        img = findViewById(R.id.pic)
        reset.setOnClickListener(this)
        signout.setOnClickListener(this)
        update.setOnClickListener(this)
        picup.setOnClickListener(this)
        getprof = registerForActivityResult(ActivityResultContracts.GetContent()){
            it.let{
                uri = it
                it?.let { it1 -> storage.child("/profile/${auth.currentUser!!.uid}").putFile(it1).addOnCompleteListener { if(it.isSuccessful){
                    Toast.makeText(this,"Profile Updated",Toast.LENGTH_LONG).show()
                    profilepic()
                }
                else
                {
                    Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                }} }
                img.setImageURI(it)


            }

        }
        if(auth.currentUser != null){
            getdata(auth.currentUser!!.uid)
            profilepic()

            }
        else{
            Intent(this,Loginacc::class.java).also { startActivity(it) }
        }
    }
    override fun onClick(v: View?){
        if(v?.id == R.id.signout){
            auth.signOut()
            Intent(this,Dashboardact::class.java).also { startActivity(it) }
        }
        if(v?.id == R.id.picu){
            val pickim = Intent(Intent.ACTION_GET_CONTENT)
            getprof.launch("image/*")
        }
    }
    fun getdata(uid:String){
        db.child(uid).get().addOnSuccessListener { data -> if(data.exists()){
            dbname.text = "   ".plus(data.child("username").value.toString())
            dbemail.text = "   ".plus(data.child("email").value.toString())
            dbphone.text = "   ".plus(data.child("phone").value.toString())
            sharen = dbname.text.toString()
        }
            else{
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
        }
        }

        }
    fun profilepic(){
        var setpic = storage.child("/profile/${auth.currentUser!!.uid}").downloadUrl.addOnSuccessListener {
            if(it != null) {
                Picasso.get().load(it).into(img)
            }
            else{
                Toast.makeText(this,"Profile Update Failed",Toast.LENGTH_LONG)
            }
        }
    }
}

