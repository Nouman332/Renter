package com.example.renterandroidapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.renterandroidapp.model.Urls
import java.util.Objects

class ImageAdapter(var context: Context?,var startorders: List<Urls>?) : PagerAdapter() {



    override fun getCount(): Int {
        return startorders?.size!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    // private int[] mImageIds = new int[]{R.drawable.bee, R.drawable.cello, R.drawable.lion, R.drawable.pumpkin, R.drawable.watch};



    override fun instantiateItem(container: ViewGroup, position: Int): ImageView {
        val startorder: Urls = startorders!![position]
        val imageView = ImageView(context)
        Glide.with(context!!)
            .load(startorder.url)
            .into(imageView)
        container.addView(imageView, 0)
        return imageView
    }

     override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView?)
    }
}