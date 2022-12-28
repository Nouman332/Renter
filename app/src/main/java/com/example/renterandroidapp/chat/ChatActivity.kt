package com.example.renterandroidapp.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.renterandroidapp.R
import com.example.renterandroidapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatActivity : AppCompatActivity() {
    private lateinit var chatList: MutableList<ChatMessages>
    private var currentUserID: String = "0"
    private var chatUID: String = "0"
    private var ownerID: String = "0"
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noChatFound: TextView
    private lateinit var progressbar: ProgressBar
    private lateinit var txtChatBox: EditText
    private lateinit var txtSend: ImageView

    // Firebase Part//
    lateinit var myRef: DatabaseReference
    lateinit var firebaseAuthentication: FirebaseAuth
    val database = FirebaseDatabase.getInstance()
    // Firebase Part Ending//
    var count : Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_chat)

        chatUID = intent.getStringExtra("chatUID").toString()

        ownerID = intent.getStringExtra("addID").toString()

        setViews()
        firebaseAuthentication = FirebaseAuth.getInstance()
        myRef = database.getReference("Chat")

        // var currentUser =  firebaseAuthentication.currentUser?.uid


        chatList = ArrayList()
        currentUserID = firebaseAuthentication.currentUser?.uid.toString()

        myRef.child("userChats").child(ownerID).child(currentUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        chatUID = snapshot.child("chatUID").value.toString()
                    } else {
                        chatUID = SimpleDateFormat("ddMMyyyyhhmmss").format(Date())
                        database.getReference("Users").child("OwnerAccount").child(ownerID).addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshots: DataSnapshot) {
                                val ownerName = snapshots.child("name").value.toString()

                                database.getReference("Users").child("RenterAccount")
                                    .child(currentUserID)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshotss: DataSnapshot) {
                                            val currentUserName =
                                                snapshotss.child("name").value.toString()
                                            val taskMap = (hashMapOf<String, Any>(
                                                "chatUID"   to chatUID,
                                                "OwnerName" to ownerName,
                                                "RenterName" to currentUserName
                                            ))
                                            myRef.child("userChats").child(ownerID)
                                                .child(currentUserID)
                                                .setValue(taskMap).addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        toast("data is added")
                                                    } else {

                                                    }
                                                }
                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }
                                    })

                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                    }
                    fetchDataFromDataBase()

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

/*        if (ownerID=="0"){
            fetchDataFromDataBaseWithDirectChat()
        }else{
            fetchDataFromDataBase()
        }*/
        val sdf = SimpleDateFormat("hh:mm a")
        val currentDate = sdf.format(Date())


        txtSend.setOnClickListener {

            if (txtChatBox.text.isEmpty()) {
                Toast.makeText(this@ChatActivity, "Text must not be empty", Toast.LENGTH_LONG)
                    .show()
            } else {
                val chatID = SimpleDateFormat("ddMMyyyyhhmmss").format(Date())

                myRef.child("chatMessages").child(chatUID).child(chatID).setValue(
                    ChatMessages(
                        "pe6vBH0vdXN2zHzNPPxrPccP4nL2",
                        "22-12:2050",
                        currentDate,
                        txtChatBox.text.toString(),
                        getString(R.string.from_renter),
                        ""
                    )
                ).addOnCompleteListener {
                    txtChatBox.text.clear()
                }

            }
        }

    }


    private fun setViews() {
        recyclerView = findViewById(R.id.recyclerView)
        noChatFound = findViewById(R.id.noChatFound)
        progressbar = findViewById(R.id.progressbar)
        txtChatBox = findViewById(R.id.txtChatBox)
        txtSend = findViewById(R.id.send)
    }

    private fun fetchDataFromDataBase() {

        myRef.child("chatMessages").child(chatUID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        chatList = ArrayList()

                        var map: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
                        if (snapshot.hasChildren()) {
                            val iterator: Iterator<*> = map.entries.iterator()
                            while (iterator.hasNext()) {
                                val me2 = iterator.next() as Map.Entry<*, *>

                                val requestDetails = me2.value as HashMap<String, Any>
                                //   Log.e("HasMapE",requestDetails.toString())
                                chatList.add(
                                    ChatMessages(
                                        requestDetails["sentBy"].toString(),
                                        requestDetails["messageDate"].toString(),
                                        requestDetails["messageTime"].toString(),
                                        requestDetails["message"].toString(),
                                        requestDetails["sender"].toString()
                                    )
                                )

                            }




                            if (chatList.isEmpty()) {
                                noChatFound.visibility = View.VISIBLE
                                progressbar.visibility = View.GONE
                                recyclerView.visibility = View.GONE

                            } else {
                                recyclerView.visibility = View.VISIBLE
                                noChatFound.visibility = View.GONE
                                progressbar.visibility = View.GONE
                            }
                        }


                        recyclerView.adapter = ChatAdapter(this@ChatActivity, chatList)

                        recyclerView.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )


                    } catch (ex: java.lang.Exception) {

                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    private fun fetchDataFromDataBaseWithDirectChat(chatUID: String) {


        myRef.child("userChats").child(ownerID).child(currentUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                chatUID = if (snapshot.hasChildren()){
//                    snapshot.child("chatUID").value.toString()
//                }else{
//                    SimpleDateFormat("ddMMyyyyhhmmss").format(Date())
//                }

                    myRef.child("chatMessages").child(chatUID)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                try {
                                    progressbar.visibility = View.GONE
                                    chatList = ArrayList()

                                    var map: HashMap<String, Any> =
                                        snapshot.value as HashMap<String, Any>
                                    if (snapshot.hasChildren()) {
                                        val iterator: Iterator<*> = map.entries.iterator()
                                        while (iterator.hasNext()) {
                                            val me2 = iterator.next() as Map.Entry<*, *>

                                            val requestDetails = me2.value as HashMap<String, Any>
                                            //   Log.e("HasMapE",requestDetails.toString())
                                            chatList.add(
                                                ChatMessages(
                                                    requestDetails["sentBy"].toString(),
                                                    requestDetails["messageDate"].toString(),
                                                    requestDetails["messageTime"].toString(),
                                                    requestDetails["message"].toString(),
                                                    requestDetails["sender"].toString()
                                                )
                                            )

                                        }




                                        if (chatList.isEmpty()) {
                                            noChatFound.visibility = View.VISIBLE
                                            progressbar.visibility = View.GONE
                                            recyclerView.visibility = View.GONE

                                        } else {
                                            recyclerView.visibility = View.VISIBLE
                                            noChatFound.visibility = View.GONE
                                            progressbar.visibility = View.GONE
                                        }
                                    }


                                    recyclerView.adapter = ChatAdapter(this@ChatActivity, chatList)

                                    recyclerView.layoutManager = LinearLayoutManager(
                                        applicationContext,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )


                                } catch (ex: java.lang.Exception) {

                                }


                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


    }

}