package com.example.renterandroidapp.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.renterandroidapp.R
import com.example.renterandroidapp.dashboard.fragments.FavouriteFragment
import com.example.renterandroidapp.dashboard.fragments.HomeFragments
import com.example.renterandroidapp.dashboard.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home_page)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer,
            HomeFragments()).commit()

        bottomNav = findViewById(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                        HomeFragments()).commit()
                    true
                }
                R.id.favourite -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                        FavouriteFragment()).commit()
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                        ProfileFragment()).commit()
                    true
                }
                else -> {

                    false
                }
            }
        }


    }


}