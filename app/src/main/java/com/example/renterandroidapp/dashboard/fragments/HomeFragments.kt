package com.example.renterandroidapp.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.renterandroidapp.R
import com.example.renterandroidapp.adapter.DashboardAdapterCategory
import com.example.renterandroidapp.adapter.DashboardAdapterFeature
import com.example.renterandroidapp.model.DashboardModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HomeFragments : Fragment() {

    lateinit var recyclerViewByCategory : RecyclerView
    lateinit var recyclerViewFeature : RecyclerView

    lateinit var dashboardAdapterCategory: DashboardAdapterCategory
    lateinit var dashboardAdapterFeature: DashboardAdapterFeature

    private lateinit var mList: List<DashboardModel>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setRecyclerViewByCategory()
        setRecyclerViewFeature()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragments, container, false)
    }

    private fun setRecyclerViewByCategory() {
//        mList = ArrayList()
//        dashboardAdapterCategory=DashboardAdapterCategory(mList,requireContext())
//        recyclerViewByCategory.layoutManager=
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
//        recyclerViewByCategory.adapter=dashboardAdapterCategory
//
//        myRef.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                val list: List<AddDataModel> = snapshot.children.map { dataSnapshot ->
//                    dataSnapshot.getValue(AddDataModel::class.java)!!
//                }
//
//                recyclerViewByCategory.adapter=ListingsAdapter(list,requireContext())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        } )

    }

    private fun setRecyclerViewFeature() {
        mList = ArrayList()
        dashboardAdapterFeature=DashboardAdapterFeature(mList,requireContext())
        recyclerViewFeature.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        recyclerViewFeature.adapter=dashboardAdapterFeature
    }

    private fun initView(view: View) {
        recyclerViewByCategory = view.findViewById(R.id.recyclerViewByCategory)
        recyclerViewFeature = view.findViewById(R.id.recyclerViewFeature)
    }
}