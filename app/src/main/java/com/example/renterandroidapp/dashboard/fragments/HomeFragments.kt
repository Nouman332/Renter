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
        mList = ArrayList()
        recyclerViewByCategory.adapter=DashboardAdapterCategory(mList,requireContext())
        recyclerViewByCategory.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
    }

    private fun setRecyclerViewFeature() {
        mList = ArrayList()
        recyclerViewFeature.adapter=DashboardAdapterFeature(mList,requireContext())
        recyclerViewFeature.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
    }

    private fun initView(view: View) {
        recyclerViewByCategory = view.findViewById(R.id.recyclerViewByCategory)
        recyclerViewFeature = view.findViewById(R.id.recyclerViewFeature)
    }
}