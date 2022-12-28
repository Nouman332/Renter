package com.example.renterandroidapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.renterandroidapp.R
import com.example.renterandroidapp.booking.BookingReview

class DialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        return inflater.inflate(R.layout.custom_dialog_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        val confirm : Button= dialog!!.findViewById(R.id.btnConfirm)
        val cancel: Button = dialog!!.findViewById(R.id.btnCancel)

        confirm.setOnClickListener {

            (activity as BookingReview?)?.confirmBooking()

        }

            cancel.setOnClickListener {
                      dialog!!.dismiss()
            }
        }

    }

