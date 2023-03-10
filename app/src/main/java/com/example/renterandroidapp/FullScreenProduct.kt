package com.example.renterandroidapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.renterandroidapp.adapter.ImageAdapter
import com.example.renterandroidapp.booking.BookingReview
import com.example.renterandroidapp.chat.ChatActivity
import com.example.renterandroidapp.chat.ChatMainActivity
import com.example.renterandroidapp.databinding.ActivityFullScreenProductBinding
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.Urls
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FullScreenProduct : AppCompatActivity() {
    var addId: String =""
    var uid: String =""
    private lateinit var binding : ActivityFullScreenProductBinding
    lateinit var firebaseauthentication: FirebaseAuth
    val database = FirebaseDatabase.getInstance()
    lateinit var myRef: DatabaseReference
    var addDetail: AddDataModel = AddDataModel()
    var viewPager: ViewPager? = null
    var adapter: ImageAdapter? = null
    var  urlList: ArrayList<Urls> = ArrayList()
    var status: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityFullScreenProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseauthentication = FirebaseAuth.getInstance()  //firebase variable initialize
        myRef = database.reference
        val intent = intent
        addId=intent.getStringExtra("addId").toString()
        uid=intent.getStringExtra("uid").toString()

        apiCall()

        myRef=database.reference.child("Bookings").child(addId)
        myRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    status= snapshot.child("status").getValue().toString()
                    if (status.equals("Pending")){
                        binding.txtBook.text="Book Now"
                    }
                    else if (status.equals("Requested")){
                        binding.txtBook.text="Booking Requested by other user"
                        binding.txtBook.isClickable=false
                        binding.txtBook.setBackgroundColor(Color.RED)
                    }
                    else
                    {
                        binding.txtBook.text="Booked by other user"
                        binding.txtBook.isClickable=false
                        binding.txtBook.setBackgroundColor(Color.RED)

                    }


                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        binding.txtBook.setOnClickListener {

            startActivity(Intent(this@FullScreenProduct,BookingReview::class.java).putExtra("uid",uid).putExtra("addId",addId))

        }
        binding.txtMesg.setOnClickListener {
            if (addID == "") {
                toast("Owner details is not available! you can't chat now. Please try later.")
            } else {
                startActivity(
                    Intent(
                        this@FullScreenProduct,
                        ChatActivity::class.java
                    ).putExtra("chatUID", uid).putExtra("addID",addID)
                )
            }
        }




    }
    var addID = ""
    fun apiCall() {

        myRef.child("OwnerAdd").child(uid.toString()).child(addId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                {
                    addDetail = dataSnapshot.getValue(AddDataModel::class.java)!!
                    binding.propertyName.text = addDetail.propertyType.toString()
                    binding.txtPrice.text = addDetail.Price.toString()
                    binding.location.text = addDetail.Address.toString()
                    addID = addDetail.uid.toString()
                    myRef = database.getReference("Users").child("OwnerAccount").child(addDetail.uid.toString())
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){

                                binding.uDname.text=snapshot.child("name").value.toString()
                                binding.uDnumber.text=snapshot.child("phone").value.toString()
                                binding.uDemail.text=snapshot.child("email").value.toString()
                                binding.uDCnic.text=snapshot.child("cnic").value.toString()
                                binding.uDtype.text=snapshot.child("accountType").value.toString()
                                binding.uDjoinDate.text=snapshot.child("joinDate").value.toString()

                                myRef = database.getReference("photos").child(addId) //userId
                                myRef.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()){
                                            for (snapShot1  in snapshot.children ){
                                                val urls : Urls? = snapShot1.getValue(Urls::class.java)
                                                urls?.let { urlList.add(it) }
                                            }
                                            adapter=ImageAdapter(this@FullScreenProduct,urlList)
                                            binding.viewPager.adapter= adapter

                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                }

            }


            override fun onCancelled(databaseError: DatabaseError) {
            }
        })


    }
}