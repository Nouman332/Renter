package com.example.renterandroidapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide

import com.example.renterandroidapp.FullScreenProduct
import com.example.renterandroidapp.R
import com.example.renterandroidapp.dashboard.HomePage
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.MainImage
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class DashboardAdapterCategory(val custinfos: ArrayList<AddDataModel>,private var context:Context) : Adapter<DashboardAdapterCategory.ViewHolder>(),Filterable {

    val database = FirebaseDatabase.getInstance()
    var myRef: DatabaseReference =database.reference
    val bundle : Bundle = Bundle()
    var MainImage : MainImage= MainImage("","")
    var custinfosF: ArrayList<AddDataModel> = custinfos

    var status : String =""



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_recyclerview_item, parent, false)

        return ViewHolder(view)
    }

    fun updateData(newList:ArrayList<AddDataModel>) {

        val newSize = newList.size
        if (newList != null)
            this.custinfos.addAll(newList)
        notifyItemRangeInserted(0, newSize)


    }
    fun clearItems() {
        val oldSize = this.custinfos.size
        this.custinfos.clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        this.custinfosF=custinfos
        val dashboardModel : AddDataModel=custinfosF[position]

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
                                MainImage= snapshot.getValue(MainImage::class.java)!!
                                Glide.with(holder.itemView.getContext())
                                    .load(MainImage.url)
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



        holder.date.text = dashboardModel.time
        holder.name.text = dashboardModel.description
        holder.city.text=dashboardModel.city
        holder.area.text=dashboardModel.area
    holder.itemView.setOnClickListener {

      context.startActivity(Intent(context,FullScreenProduct::class.java).putExtra("uid",dashboardModel.uid).putExtra("addId",dashboardModel.addPhotosUrl))
    }
    }

    override fun getItemCount(): Int {
        return custinfosF.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.propertyImage)
        val name: TextView = itemView.findViewById(R.id.propertyName)
        val date: TextView = itemView.findViewById(R.id.PostDate)
        val city : TextView = itemView.findViewById(R.id.propertyCity)
        val area: TextView =itemView.findViewById(R.id.propertyCityArea)
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charS = charSequence.toString()
                val filteredList: MutableList<AddDataModel> = ArrayList()
                if (charS.isEmpty()) {
                    custinfosF = custinfos
                } else {
                    for (row in custinfos) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.city!!
                                .contains(charSequence) || row.area!!
                                .contains(charSequence) || row.description!!.contains(charSequence)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    custinfosF = filteredList as ArrayList<AddDataModel>
                    notifyDataSetChanged()
                }
                val filterResults = FilterResults()
                filterResults.values = custinfosF
                return filterResults
            }

             override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                notifyItemRangeInserted(0, custinfosF.size)
            }
        }
    }
}