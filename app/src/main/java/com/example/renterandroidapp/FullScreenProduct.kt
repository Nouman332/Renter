package com.example.renterandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.renterandroidapp.booking.BookingReview

class FullScreenProduct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_full_screen_product)
        findViewById<ImageView>(R.id.imgBack).setOnClickListener {
            finish()
        }
        findViewById<ImageView>(R.id.imgMap).setOnClickListener {
            startActivity(Intent(this@FullScreenProduct,MapsActivity::class.java))
        }

        findViewById<TextView>(R.id.txtBuy).setOnClickListener {
            startActivity(Intent(this@FullScreenProduct,BookingReview::class.java))
        }

    }
}