package com.example.itbatch2021

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MsgAdapt(val msg:List<Messages>): RecyclerView.Adapter<MsgAdapt.Viewholder>(){
    var count = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgAdapt.Viewholder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.msglay,parent,false)
        return Viewholder(itemview)
    }

    override fun onBindViewHolder(holder: MsgAdapt.Viewholder, position: Int) {
        val cur = msg[position]
        holder.sender.text = cur.sender
        holder.msg.text = cur.message
        holder.time.text = cur.time
    }

    override fun getItemCount():Int{
       return msg.size
    }
    class Viewholder(view:View):RecyclerView.ViewHolder(view){
        val sender:TextView = view.findViewById(R.id.tv_sender)
        val msg:TextView = view.findViewById(R.id.tv_message)
        val time:TextView = view.findViewById(R.id.time)

    }
}