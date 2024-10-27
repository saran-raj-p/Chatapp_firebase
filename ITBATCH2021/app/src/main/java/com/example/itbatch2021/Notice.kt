package com.example.itbatch2021
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itbatch2021.databinding.NoticeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

    class Notice:Navigation(),View.OnClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var noticeBinding: NoticeBinding
    lateinit var auth: FirebaseAuth
    lateinit var upload: Button
    var filedb = Firebase.firestore
    var fileinfo = mutableMapOf<String, Uri>()
    var setinfo = mutableListOf<Noticedata>()
    lateinit var byt: TextView
    lateinit var prog: ProgressBar
    lateinit var laun: ActivityResultLauncher<String>
    var curf: Uri? = null
        var fname:String = ""
    lateinit var flink: Uri
    var getcount: Int = 0
    var noticelist: MutableList<Noticedata> = mutableListOf()
    lateinit var filen: String
    var storage = Firebase.storage.reference
    var notice = storage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noticeBinding = NoticeBinding.inflate(layoutInflater)
        setContentView(noticeBinding.root)
        upload = findViewById(R.id.upload)
        prog = findViewById(R.id.progress)
        //select = findViewById(R.id.select)
        recyclerView = findViewById(R.id.notice)
        byt = findViewById(R.id.uploaded)
        recyclerView.layoutManager = LinearLayoutManager(this)
        upload.setOnClickListener(this)
        //select.setOnClickListener(this)
        recyclerView.setHasFixedSize(true)
        laun = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                curf = uri
                var tmpname = curf.toString()
                curf?.let {
                    prog.visibility = View.VISIBLE
                    notice = storage.child("/notice/${tmpname.substringAfterLast("/")}")
                    val upfile = notice.putFile(it)
                    upfile.addOnProgressListener {
                        val progress = (100 * it.bytesTransferred) / it.totalByteCount
                        byt.text = progress.toString().plus("%")
                    }
                    upfile.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            prog.visibility = View.GONE
                            byt.visibility = View.GONE
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()
                            storage.child("/notice/${tmpname.substringAfterLast("/")}").downloadUrl.addOnSuccessListener {
                                Log.d("Link", "$it")
                                Log.d("tmp","$tmpname")
                                Log.d("path","${tmpname.substringAfterLast("/")}")
                                flink = it
                                filedb.collection("notice").get().addOnSuccessListener {
                                    Log.d("count", "${it.size()}")
                                    getcount = it.size()
                                }
                                fileinfo.put(curf?.lastPathSegment.toString(), flink)
                                filedb.collection("notice").document("pdfs").set(fileinfo,
                                    SetOptions.merge())
                                fileinfo.clear()
                                //                                    filedb.collection("notice").add()
                            }

                        } else {
                            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        getreal()
        //getall()
    }
    fun getreal(){
        filedb.collection("notice").document("pdfs").addSnapshotListener(this) {  value, error ->
            if(error != null){
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                //value.size()
                setinfo.clear()
                    for((keys,values) in value.data!!) {
                        var fname = if(keys.length > 7) keys.drop(8) else keys

                        var link = values
                        Log.d("Key", "${keys}")
                        Log.d("Data", "${values}")
                        setinfo.add(Noticedata(fname, Uri.parse(link.toString())))
                    }
                recyclerView.adapter = Noticeadapt(this,setinfo)
                    }
        }

    }


    override fun onClick(view: View) {
        if (view.id == R.id.upload) {
            selectfile()
        }
    }

        override fun onResume() {
            super.onResume()
            setinfo.clear()
        }

        override fun onPause() {
            super.onPause()
            setinfo.clear()
        }

    @SuppressLint("NewApi")
    private fun selectfile() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        laun.launch("*/*")
    }
}



