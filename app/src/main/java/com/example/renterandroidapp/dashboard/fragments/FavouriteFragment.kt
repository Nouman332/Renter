package com.example.renterandroidapp.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.renterandroidapp.R
import com.example.renterandroidapp.adapter.BookingsAdapter
import com.example.renterandroidapp.adapter.DashboardAdapterCategory
import com.example.renterandroidapp.model.AddBooking
import com.example.renterandroidapp.model.AddDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavouriteFragment : Fragment() {
    lateinit var recyclerViewBookings : RecyclerView
    lateinit var bookingsAdapter: BookingsAdapter
    private lateinit var mList: ArrayList<AddBooking>
    lateinit var firebaseauthentication: FirebaseAuth
    val database = FirebaseDatabase.getInstance()
    lateinit var myRef: DatabaseReference
    lateinit var booking: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myRef=database.reference
        firebaseauthentication=FirebaseAuth.getInstance()
        booking=view.findViewById(R.id.bookingsTxt)



        initView(view)
        fetchBookings()



    }

    private fun fetchBookings() {
        var renterId: String=firebaseauthentication.currentUser?.uid.toString()
        myRef = database.reference.child("Bookings")
        val query : Query = myRef.orderByChild("renterId").equalTo(renterId)
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList= snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(AddBooking::class.java)!!
                } as ArrayList<AddBooking>
                bookingsAdapter.updateData(mList)
                booking.text=mList.size.toString()+" Bookings"
            }
            override fun onCancelled(error: DatabaseError) {
            }
        } )
    }


    private fun initView(view: View) {
        recyclerViewBookings = view.findViewById(R.id.recyclerviewBookings)
        mList = ArrayList()
        recyclerViewBookings.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        bookingsAdapter= BookingsAdapter(mList,requireContext())
        recyclerViewBookings.adapter=bookingsAdapter
    }

}