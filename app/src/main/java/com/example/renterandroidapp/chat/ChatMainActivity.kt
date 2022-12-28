package com.example.renterandroidapp.chat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.renterandroidapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatMainActivity : AppCompatActivity() {

    private lateinit var chatList: MutableList<ChatMainMessages>
    private lateinit var currentUserID: String
    private lateinit var recyclerView: RecyclerView
    // Firebase Part//
    lateinit var myRef: DatabaseReference
    lateinit var firebaseAuthentication: FirebaseAuth
    val database = FirebaseDatabase.getInstance()
    // Firebase Part Ending//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_chat_main)

        setViews()
        firebaseAuthentication = FirebaseAuth.getInstance()
        myRef = database.getReference("Chat")




        chatList = ArrayList()
        currentUserID = firebaseAuthentication.currentUser?.uid.toString()

        fetchDataFromDataBase()
        val sdf = SimpleDateFormat("hh:mm a")
        val currentDate = sdf.format(Date())




    }

    private fun setViews() {
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun fetchDataFromDataBase() {

        myRef.child("userChats").child(currentUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        chatList = java.util.ArrayList()

                        val map: java.util.HashMap<String, Any> = snapshot.value as java.util.HashMap<String, Any>
                        if (snapshot.hasChildren()) {
                            val iterator: Iterator<*> = map.entries.iterator()
                            while (iterator.hasNext()) {
                                val me2 = iterator.next() as Map.Entry<*, *>

                                val requestDetails = me2.value as java.util.HashMap<String, Any>
                                //   Log.e("HasMapE",requestDetails.toString())
                                chatList.add(
                                    ChatMainMessages(
                                        requestDetails["chatUID"].toString(),
                                        //map["lastMessageSent"].toString()
                                        requestDetails["RenterName"].toString()
                                    )
                                )

                            }
                        }





                        if (chatList.isEmpty()) {
                            recyclerView.visibility = View.GONE

                        } else {
                            recyclerView.visibility = View.VISIBLE


                        }


                        recyclerView.adapter = ChatMainAdapter(this@ChatMainActivity, chatList)

                        recyclerView.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )


                    }catch (ex: java.lang.Exception){

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

}