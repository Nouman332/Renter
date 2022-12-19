package com.example.renterandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.example.renterandroidapp.sharedpreference.SharedPref

class MainActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        SharedPref.init(applicationContext)

        progressBar = findViewById(R.id.progressBar)
        Handler().postDelayed({
            progressBar.visibility=View.GONE
            findViewById<RelativeLayout>(R.id.relativeLetsStart).visibility= View.VISIBLE
        },3000)
        findViewById<RelativeLayout>(R.id.relativeLetsStart).setOnClickListener {
            if(SharedPref.read("isAppFistTime",true)){
                val intent = Intent(this, OnBoardingScreen::class.java)
                startActivity(intent)
                finish()
            }else{
              //  val intent = Intent(this, Login::class.java)
                val intent = Intent(this, OnBoardingScreen::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}