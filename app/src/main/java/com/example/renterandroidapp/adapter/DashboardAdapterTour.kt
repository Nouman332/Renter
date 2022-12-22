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
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.DashboardModel


class DashboardAdapterTour(private val mList: ArrayList<AddDataModel>, private var context: Context) : Adapter<DashboardAdapterTour.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_recyclerview_feature_item, parent, false)

        return ViewHolder(view)
    }
    fun updateData(newList:ArrayList<AddDataModel>) {

        clearItems()
        val newSize = newList.size
        if (newList != null)
            this.mList.addAll(newList)
        notifyItemRangeInserted(0, newSize)


    }
    fun clearItems() {
        val oldSize = this.mList.size
        this.mList.clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      /*  val dashboardModel=mList[position]
        holder.imageView.setImageResource(dashboardModel.image)
        holder.date.text = dashboardModel.date
        holder.name.text = dashboardModel.name*/
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, FullScreenProduct::class.java))
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