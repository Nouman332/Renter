package com.example.renterandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.renterandroidapp.adapter.ViewPagerAdapter
import com.example.renterandroidapp.fragment.OnBoardingFragment
import com.example.renterandroidapp.sharedpreference.SharedPref
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnBoardingScreen : AppCompatActivity() , View.OnClickListener {
    var iFragments: MutableList<Fragment> = ArrayList()
    private lateinit var txtBtnNext : TextView
    private lateinit var dotsIndicator : DotsIndicator
    private lateinit var viewPager : ViewPager2
    private lateinit var fragment1 : OnBoardingFragment
    private lateinit var fragment2 : OnBoardingFragment
    private lateinit var fragment3 : OnBoardingFragment
    private lateinit var txtSkipbtn : TextView
    private lateinit var txtDescription : TextView
    private lateinit var txtSearchExpert : TextView



    private  var currentPageNo:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        setContentView(R.layout.activity_on_boarding_screen)

        initView()
        setViewListeners()


    }
    private fun initView(){
        txtBtnNext=findViewById(R.id.txtBtnNext)
        dotsIndicator=findViewById(R.id.dotsIndicator)
        viewPager=findViewById(R.id.viewPager)
        txtSearchExpert=findViewById(R.id.txtSearchExpert)
        txtDescription=findViewById(R.id.txtDescription)
        txtSkipbtn=findViewById(R.id.txtSkipBtn)
        fragment1= OnBoardingFragment()
        fragment2= OnBoardingFragment()
        fragment3=OnBoardingFragment()


        ContextCompat.getDrawable(this,R.drawable.onb)?.let { fragment1.setImage(it) }
        ContextCompat.getDrawable(this,R.drawable.onbtwo)?.let { fragment2.setImage(it) }
        ContextCompat.getDrawable(this,R.drawable.onbthree)?.let { fragment3.setImage(it) }


        iFragments.add(fragment1)
        iFragments.add(fragment2)
        iFragments.add(fragment3)

        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle,iFragments)
        viewPager.adapter = adapter
        dotsIndicator.setViewPager2(viewPager)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0->{
                        txtBtnNext.text="Next"
                        txtSearchExpert.text="Find best place "
                        txtDescription.text="to stay in good price"
                        txtSkipbtn.visibility= View.VISIBLE

                    }
                    1->{
                        txtBtnNext.text="One More"
                        txtSearchExpert.text="Fast sell your property"
                        txtDescription.text="in just one click"
                        txtSkipbtn.visibility= View.VISIBLE
                    }
                    2->{
                        txtBtnNext.text="Let's Started"
                        txtSearchExpert.text="Find perfect choice for"
                        txtDescription.text="Your future house"
                        txtSkipbtn.visibility= View.GONE
                    }
                }

                currentPageNo=position
            }
        })


    }
    private fun setViewListeners() {
        txtBtnNext.setOnClickListener(this)
        txtSkipbtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txtBtnNext -> {
//                val intent = Intent(this, OnBoardingScreen::class.java)
//                startActivity(intent)
                if (currentPageNo==2)
                {
                    SharedPref.write("isAppFistTime",false)
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    viewPager.postDelayed({ viewPager.currentItem = ++currentPageNo }, 50)


                }

            }
            R.id.txtSkipBtn -> {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

}