package com.example.renterandroidapp.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.renterandroidapp.R

class BookingReview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_booking_review)
    }
}