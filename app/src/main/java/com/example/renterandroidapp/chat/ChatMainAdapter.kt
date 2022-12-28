package com.example.renterandroidapp.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.renterandroidapp.R


class ChatMainAdapter(
    var context: Context,
    var notesList: List<ChatMainMessages>,

    ) : RecyclerView.Adapter<ChatMainAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_main_contact_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.txtLastMessage.text = notesList[position].lastMessage

        holder.itemView.setOnClickListener{
            context.startActivity(Intent(context, ChatActivity::class.java).putExtra("chatUID",notesList[position].chatUID))
        }





    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtLastMessage: TextView

        init {
            txtLastMessage = view.findViewById(R.id.txtLastMessage)

        }
    }
}