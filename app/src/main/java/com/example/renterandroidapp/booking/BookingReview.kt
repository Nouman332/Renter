package com.example.renterandroidapp.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.renterandroidapp.adapter.ImageAdapter
import com.example.renterandroidapp.dashboard.HomePage
import com.example.renterandroidapp.databinding.ActivityBookingReviewBinding
import com.example.renterandroidapp.fragment.DialogFragment
import com.example.renterandroidapp.model.AddBooking
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.MainImage
import com.example.renterandroidapp.model.Urls
import com.example.renterandroidapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.paypal.android.sdk.payments.*
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal


class BookingReview : AppCompatActivity() {

    var addId: String=""
    var uid: String=""
    lateinit var binding: ActivityBookingReviewBinding
    lateinit var firebaseauthentication: FirebaseAuth
    val database = FirebaseDatabase.getInstance()
    lateinit var myRef: DatabaseReference
    var addDetail: AddDataModel = AddDataModel()
    var viewPager: ViewPager? = null
    var adapter: ImageAdapter? = null
    var  urlList: ArrayList<Urls> = ArrayList()
    var MainImage : MainImage= MainImage("","")
    lateinit var dialogFragment: DialogFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding=ActivityBookingReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myRef=database.reference
        firebaseauthentication=FirebaseAuth.getInstance()
        val intent = intent
        addId=intent.getStringExtra("addId").toString()
        uid=intent.getStringExtra("uid").toString()

        apiCall()
        binding.txtBuy.setOnClickListener {

            dialogFragment= DialogFragment()
            dialogFragment.show(supportFragmentManager, "MyCustomDialog")

        }

    }

    fun confirmBooking(){
            var renterId: String=firebaseauthentication.currentUser?.uid.toString()
               myRef=database.reference.child("Bookings").child(addId)
               myRef.setValue(
               AddBooking(
                   addId,
                   uid,
                   renterId,
                   "Requested"
               )).addOnCompleteListener{
                   if (it.isSuccessful)
                   {
                       dialogFragment.dismiss()
                       startActivity(Intent(this@BookingReview,HomePage::class.java))
                       finish()
                   }
               else
                   toast("Some error occurred Booking not Confirmed")
                   dialogFragment.dismiss()
           }

    }

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
                    binding.city.text=addDetail.city.toString()
                    binding.propertyName.text=addDetail.description.toString()
                    binding.duration.text=addDetail.Duration.toString()

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

                                myRef=database.getReference("photos").child(addDetail.addPhotosUrl.toString()).child("image0")

                                myRef.addListenerForSingleValueEvent(object: ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()){
                                            MainImage = snapshot.getValue(MainImage::class.java)!!
                                            Glide.with(this@BookingReview)
                                                .load(MainImage.url)
                                                .into(binding.propertyImage)

                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                } )
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