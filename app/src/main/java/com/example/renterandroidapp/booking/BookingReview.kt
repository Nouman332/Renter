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
import com.example.renterandroidapp.databinding.ActivityBookingReviewBinding
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.MainImage
import com.example.renterandroidapp.model.Urls
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


    val clientKey = "AXEykcQymGx6NVf6GArG0gdcUkJNuOE9Ubwz9JNmHhfUxqh9fOiNUrRZNpQ0PnICxzSFCetxn9COkKeo"
    val PAYPAL_REQUEST_CODE = 123

    // Paypal Configuration Object
    private val config = PayPalConfiguration() // Start with mock environment.  When ready,
        // switch to sandbox (ENVIRONMENT_SANDBOX)
        // or live (ENVIRONMENT_PRODUCTION)
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // on below line we are passing a client id.
        .clientId(clientKey)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding=ActivityBookingReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myRef=database.reference
        val intent = intent
        addId=intent.getStringExtra("addId").toString()
        uid=intent.getStringExtra("uid").toString()

        apiCall()
        binding.txtBuy.setOnClickListener {

            getPayment()

        }

    }
    private fun getPayment() {

        // Getting the amount from editText
        val amount: String = "10"

        // Creating a paypal payment on below line.
        val payment = PayPalPayment(
            BigDecimal(amount), "IN", "Booking Fees",
            PayPalPayment.PAYMENT_INTENT_SALE
        )

        // Creating Paypal Payment activity intent
        val intent = Intent(this, PaymentActivity::class.java)

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)

        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        val PAYPAL_REQUEST_CODE =123
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == RESULT_OK) {

                // Getting the payment confirmation
                val confirm =
                    data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)

                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        val paymentDetails = confirm.toJSONObject().toString(4)
                        // on below line we are extracting json response and displaying it in a text view.
                        val payObj = JSONObject(paymentDetails)
                        val payID = payObj.getJSONObject("response").getString("id")
                        val state = payObj.getJSONObject("response").getString("state")
                        Toast.makeText(this@BookingReview,"Payment $state\\n with payment id is $payID",Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e)
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                // on below line we are checking the payment status.
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line when the invalid paypal config is submitted.
                Log.i(
                    "paymentExample",
                    "An invalid Payment or PayPalConfiguration was submitted. Please see the docs."
                )
            }
        }
    }
}