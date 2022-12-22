package com.example.renterandroidapp.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.renterandroidapp.R
import com.example.renterandroidapp.adapter.DashboardAdapterCategory
import com.example.renterandroidapp.adapter.DashboardAdapterTour
import com.example.renterandroidapp.model.AddDataModel
import com.google.firebase.database.*


class HomeFragments : Fragment() {

    lateinit var recyclerViewByCategory : RecyclerView
    lateinit var recyclerViewFeature : RecyclerView

    lateinit var dashboardAdapterCategory: DashboardAdapterCategory
    lateinit var dashboardAdapterTour: DashboardAdapterTour
    lateinit var adapterCategory: DashboardAdapterCategory
    private lateinit var mList: ArrayList<AddDataModel>
    lateinit var myRef: DatabaseReference
    val database = FirebaseDatabase.getInstance()
    lateinit var search: EditText


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myRef=database.getReference("OwnerAdd")
        search=view.findViewById(R.id.search)
        initView(view)
        setRecyclerViewByCategory()
        setRecyclerViewFeature()

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                updateRecyclerListWithFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragments, container, false)
    }

    private fun setRecyclerViewByCategory() {

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapShot1 : DataSnapshot  in snapshot.children ) {
                    mList = snapShot1.children.map { dataSnapshot ->
                        dataSnapshot.getValue(AddDataModel::class.java)!!
                    } as ArrayList<AddDataModel>
                }
                dashboardAdapterCategory.updateData(mList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        } )

    }
    fun updateRecyclerListWithFilter(data: String?) {
        if (dashboardAdapterCategory != null) {
            dashboardAdapterCategory.getFilter()?.filter(data)
            dashboardAdapterCategory.notifyDataSetChanged()
        }
    }

    private fun setRecyclerViewFeature() {
        mList = ArrayList()
        dashboardAdapterTour=DashboardAdapterTour(mList,requireContext())
        recyclerViewFeature.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        recyclerViewFeature.adapter=dashboardAdapterTour
    }

    private fun initView(view: View) {
        recyclerViewByCategory = view.findViewById(R.id.recyclerViewByCategory)
        recyclerViewFeature = view.findViewById(R.id.recyclerViewFeature)
        mList = ArrayList()
        dashboardAdapterCategory=DashboardAdapterCategory(mList,requireContext())
        recyclerViewByCategory.layoutManager=
            NpaLinearLayoutManager(requireContext(), LinearLayout.HORIZONTAL,false)
        recyclerViewByCategory.adapter=dashboardAdapterCategory
    }
    private class NpaLinearLayoutManager : LinearLayoutManager {
        constructor(context: Context?) : super(context) {}
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        ) {
        }

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
        }

        /**
         * Disable predictive animations. There is a bug in RecyclerView which causes views that
         * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
         * adapter size has decreased since the ViewHolder was recycled.
         */
        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }

        override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
    }
}