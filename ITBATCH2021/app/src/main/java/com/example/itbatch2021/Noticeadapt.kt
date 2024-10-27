package com.example.itbatch2021

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class Noticeadapt(val context: Context,var notice:List<Noticedata>):RecyclerView.Adapter<Noticeadapt.Viewholder>() {
   lateinit var downreq:DownloadManager.Request
    class Viewholder(view:View):RecyclerView.ViewHolder(view){
        val fname:TextView = view.findViewById(R.id.fname)
        val butt:ImageButton = view.findViewById(R.id.download)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemview:View = LayoutInflater.from(parent.context).inflate(R.layout.noticelay,parent,false)
        return Viewholder(itemview)
    }

    override fun getItemCount(): Int {
        return  notice.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val cur = notice[position]
        holder.fname.text = cur.fname
        holder.butt.setOnClickListener { cur.uri
        Log.d("Uri","${cur.uri}")
            Log.d("cur","${cur}")
            downloadfile(cur.uri.toString(),cur.fname)

        }
    }

    fun downloadfile(dlink:String,file:String){
        var downman:DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val downdir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val dest = File(downdir,file)
        Log.d("Download", "Directory: ${downdir}")
            var downreq = DownloadManager.Request(Uri.parse(dlink))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(dest))
                .setTitle("$file").setDescription("Testing")

            var downid = downman.enqueue(downreq)
    }

}