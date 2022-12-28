package com.example.renterandroidapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.renterandroidapp.adapter.ImageAdapter
import com.example.renterandroidapp.booking.BookingReview
import com.example.renterandroidapp.dashboard.HomePage
import com.example.renterandroidapp.databinding.ActivityBookingReviewBinding
import com.example.renterandroidapp.databinding.ActivityFullScreenProductBinding
import com.example.renterandroidapp.databinding.ActivityTourFullBinding
import com.example.renterandroidapp.model.AddBooking
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.TourAddModel
import com.example.renterandroidapp.model.Urls
import com.example.renterandroidapp.sharedpreference.TourDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TourFullActivity : AppCompatActivity() {
    lateinit var binding: ActivityTourFullBinding
    var addId: String=""
    var uid: String=""
    lateinit var firebaseauthentication: FirebaseAuth
    val database = FirebaseDatabase.getInstance()
    lateinit var myRef: DatabaseReference
    var tourAddModel: TourAddModel = TourAddModel()
    var status: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityTourFullBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseauthentication = FirebaseAuth.getInstance()  //firebase variable initialize
        myRef = database.reference
        val intent = intent
        addId=intent.getStringExtra("addId").toString()
        uid=intent.getStringExtra("uid").toString()
        apiCall()

        myRef=database.reference.child("TourBookings").child(addId)
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

            TourDialogFragment().show(supportFragmentManager,"tour")

        }
    }
    fun cancel(){
        startActivity(Intent(this, BookingReview::class.java))
        finish()


    }
    fun confirmBooking(){
        var renterId: String=firebaseauthentication.currentUser?.uid.toString()
        myRef=database.reference.child("TourBookings").child(addId)
        myRef.setValue(
            AddBooking(
                addId,
                uid,
                renterId,
                "Requested"
            )
        ).addOnCompleteListener{
            if (it.isSuccessful)
            {

                startActivity(Intent(this@TourFullActivity, TourFullActivity::class.java))
                finish()
            }
            else
                toast("Some error occurred Booking not Confirmed")

        }

    }


    fun apiCall() {

        myRef.child("TourAdd").child(uid.toString()).child(addId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                {
                    tourAddModel = dataSnapshot.getValue(TourAddModel::class.java)!!
                    binding.propertyName.text = tourAddModel.description.toString()
                    binding.txtPrice.text = tourAddModel.Price.toString()
                    binding.location.text = tourAddModel.Address.toString()
                    binding.TourDays.text=   "Tour Days         :"+tourAddModel.EventDays
                    binding.TourType.text=   "Event For         :" + tourAddModel.tourType
                    binding.TourPlace.text=  "Tour At          :" + tourAddModel.Address
                    binding.TourPrice.text=  "Per Person Price :"+ tourAddModel.Price
                    binding.paymentType.text="Payment Type is :" + tourAddModel.paymentType
                    binding.TourDescription.text=tourAddModel.description

                    myRef = database.getReference("Users").child("TourOrganizerAccount").child(tourAddModel.uid.toString())
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                binding.uDname.text=snapshot.child("name").value.toString()
                                binding.uDnumber.text=snapshot.child("phone").value.toString()
                                binding.uDemail.text=snapshot.child("email").value.toString()
                                binding.uDCnic.text=snapshot.child("cnic").value.toString()
                                binding.uDtype.text=snapshot.child("accountType").value.toString()

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