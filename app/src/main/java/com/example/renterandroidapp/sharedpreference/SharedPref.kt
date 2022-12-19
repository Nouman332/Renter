package com.example.renterandroidapp.sharedpreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object SharedPref {
    private var mSharedPref:SharedPreferences? = null
    fun init(context: Context)
    {
        if (mSharedPref ==null)
        {
            mSharedPref =context.getSharedPreferences(context.packageName,Activity.MODE_PRIVATE)
        }

    }
    fun read(key:String,defValue: Boolean):Boolean{
       return mSharedPref!!.getBoolean(key,defValue)
    }
    fun write(key:String,value: Boolean) {
        mSharedPref!!.edit().putBoolean(key,value).commit()
    }
}