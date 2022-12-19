package com.example.renterandroidapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.renterandroidapp.R


class OnBoardingFragment : Fragment() {
 private lateinit var imgBackground:ImageView
 private lateinit var drawable: Drawable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }

    private fun initView(view: View) {
        imgBackground=view.findViewById(R.id.imgBackground)
        imgBackground.setImageDrawable(drawable)
    }
    fun setImage(drawable: Drawable){
        this.drawable=drawable

    }


}