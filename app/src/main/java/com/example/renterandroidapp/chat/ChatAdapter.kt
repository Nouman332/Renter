package com.example.renterandroidapp.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.renterandroidapp.R


class ChatAdapter(
    var context: Context,
    var notesList: List<ChatMessages>,

    ) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_contact_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (notesList[getRealPosition(position)].sender == context.resources
                .getString(R.string.from_renter)
        ) {
            holder.sender_layout?.visibility = View.VISIBLE
            holder.reciver_layout?.visibility = View.GONE
            holder.contact_chat_textview_reciever?.text = notesList[getRealPosition(position)].message
            holder.txtReceiverMsgTime?.text = notesList[getRealPosition(position)].messageTime
            holder.deleted_tag?.visibility = View.GONE
        } else {
            holder.sender_layout?.visibility = View.GONE
            holder.reciver_layout?.visibility = View.VISIBLE
            val msg = notesList[getRealPosition(position)].message
            val title = notesList[getRealPosition(position)].sender
            holder.contact_chat_textview_sender?.text = msg
            holder.txtSenderMsgTime?.text = notesList[getRealPosition(position)].messageTime



        }

        /* holder.call_time_txtview?.text = notesList[getRealPosition(position)].time
         holder.call_time_txtview_reciver?.text = notesList[getRealPosition(position)].time
         holder.timestamp?.text = notesList[getRealPosition(position)].timestamp*/


    }

    override fun getItemCount(): Int {
        return notesList.size
    }
    private fun getRealPosition(position: Int): Int {
        return position
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var contact_chat_textview_sender: TextView? = null
        var contact_chat_textview_reciever:TextView? = null
        var txtReceiverMsgTime:TextView? = null
        var date_n_time:TextView? = null
        var dot: TextView? = null
        var timestamp: TextView? = null
        var call_time_txtview_reciver:TextView? = null
        var main_layout: RelativeLayout? = null
        var selected: ImageView? = null
        var sender_layout: LinearLayout? = null
        var reciver_layout:LinearLayout? = null
        var deleted_tag: TextView? = null
        var txtSenderMsgTime: TextView? = null

        init {
            contact_chat_textview_sender = view.findViewById(R.id.contact_chat_textview)
            contact_chat_textview_reciever =
                view.findViewById(R.id.contact_chat_textview_reciever)

            txtReceiverMsgTime = view.findViewById(R.id.txtReceiverMsgTime)
            date_n_time = view.findViewById(R.id.date_n_time)

            dot = view.findViewById(R.id.dot)
            timestamp = view.findViewById(R.id.timestamp)
            txtSenderMsgTime = view.findViewById(R.id.txtSenderMsgTime)

            main_layout = view.findViewById(R.id.main_layout)
            sender_layout = view.findViewById(R.id.reciver_layout)
            reciver_layout = view.findViewById(R.id.sender_layout)
        }
    }
}