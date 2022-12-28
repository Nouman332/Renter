package com.example.renterandroidapp.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.renterandroidapp.R
import com.example.renterandroidapp.model.AddBooking
import com.example.renterandroidapp.model.AddDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookingsAdapter(val mList: ArrayList<AddBooking>, var context: Context) : RecyclerView.Adapter<BookingsAdapter.ViewHolder>() {
    lateinit var firebaseauthentication: FirebaseAuth
    val database = FirebaseDatabase.getInstance()
    lateinit var myRef: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mybookings_detail, parent, false)
        return BookingsAdapter.ViewHolder(view)
    }
    fun updateData(newList:ArrayList<AddBooking>) {
        this.mList.clear()
        val newSize = newList.size
        this.mList.addAll(newList)
        notifyItemRangeInserted(0, newSize)

    }
    fun clearItems() {
        val oldSize = this.mList.size

        notifyItemRangeRemoved(0, oldSize)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var book : AddBooking=mList[position]
        myRef=database.getReference().child("OwnerAdd").child(book.ownerId.toString()).child(book.addId.toString())
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()){
                     holder.propertyName.text=snapshot.child("propertyType").value.toString()
                     holder.city.text=snapshot.child("city").value.toString()
                     holder.location.text=snapshot.child("address").value.toString()
                     holder.price.text=snapshot.child("price").value.toString()
                     holder.status.text=book.status

                     myRef=database.getReference("photos").child(snapshot.child("addPhotosUrl").value.toString()).child("image0")

                     myRef.addListenerForSingleValueEvent(object: ValueEventListener {
                         override fun onDataChange(snapshot: DataSnapshot) {
                             if (snapshot.exists()){

                                 Glide.with(context)
                                     .load(snapshot.child("url").value.toString())
                                     .into(holder.image)

                             }

                         }

                         override fun onCancelled(error: DatabaseError) {
                         }
                     } )
                 }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }


    override fun getItemCount(): Int {
    return  mList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView=itemView.findViewById(R.id.bpropertyImage)
        val propertyName: TextView= itemView.findViewById(R.id.bpropertyName)
        val city: TextView=itemView.findViewById(R.id.bcity)
        val location: TextView= itemView.findViewById(R.id.blocation)
        val price: TextView=itemView.findViewById(R.id.txtbPrice)
        val status: TextView=itemView.findViewById(R.id.txtSetStatus)

    }
}