package com.example.renterandroidapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.renterandroidapp.FullScreenProduct
import com.example.renterandroidapp.R
import com.example.renterandroidapp.model.DashboardModel


class DashboardAdapterCategory(private val mList: List<DashboardModel>,private var context:Context) : Adapter<DashboardAdapterCategory.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_recyclerview_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
/*        val dashboardModel=mList[position]
        holder.imageView.setImageResource(dashboardModel.image)
        holder.date.text = dashboardModel.date
        holder.name.text = dashboardModel.name*/
    holder.itemView.setOnClickListener {
context.startActivity(Intent(context,FullScreenProduct::class.java))
    }
    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.propertyImage)
        val name: TextView = itemView.findViewById(R.id.propertyName)
        val date: TextView = itemView.findViewById(R.id.PostDate)
    }
}