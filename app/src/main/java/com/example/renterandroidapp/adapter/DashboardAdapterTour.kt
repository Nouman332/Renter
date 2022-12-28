package com.example.renterandroidapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.renterandroidapp.FullScreenProduct
import com.example.renterandroidapp.R
import com.example.renterandroidapp.TourFullActivity
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.DashboardModel
import com.example.renterandroidapp.model.TourAddModel
import com.google.firebase.FirebaseError
import com.google.firebase.database.*


class DashboardAdapterTour(private val mList: ArrayList<TourAddModel>, private var context: Context) : Adapter<DashboardAdapterTour.ViewHolder>() {

    val database = FirebaseDatabase.getInstance()
    var myRef: DatabaseReference =database.reference
    val bundle : Bundle = Bundle()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_recyclerview_feature_item, parent, false)
        return ViewHolder(view)

    }
    fun updateData(newList:ArrayList<TourAddModel>) {
//         clearItems()
//        this.mList.clear()
        val newSize = newList.size
        if (newList != null)
            this.mList.addAll(newList)
        notifyItemRangeInserted(0, newSize)


    }
    fun clearItems() {
        val oldSize = this.mList.size
        this.mList.clear()
       // notifyItemRangeRemoved(0, oldSize)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dashboardModel=mList[position]

        myRef.child("photos").child(dashboardModel.addPhotosUrl.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //here means the value exist
                    //do whatever you want to do
                    myRef=database.getReference("photos").child(dashboardModel.addPhotosUrl.toString()).child("image0")

                    myRef.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){

                                Glide.with(context)
                                    .load(snapshot.child("url").value.toString())
                                    .into(holder.imageView)

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    } )
                } else {
                    //here means the value not exist
                    //do whatever you want to do
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            fun onCancelled(firebaseError: FirebaseError?) {}
        })


        holder.name.text = dashboardModel.description.toString()
        holder.address.text=dashboardModel.Address.toString()

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, TourFullActivity::class.java).putExtra("uid",dashboardModel.uid.toString()).putExtra("addId",dashboardModel.addPhotosUrl.toString()))
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.tourImage)
        val name: TextView = itemView.findViewById(R.id.tourName)
        val address: TextView = itemView.findViewById(R.id.tourAddress)
    }
}