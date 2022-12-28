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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.renterandroidapp.R
import com.example.renterandroidapp.adapter.DashboardAdapterCategory
import com.example.renterandroidapp.adapter.DashboardAdapterTour
import com.example.renterandroidapp.model.AddDataModel
import com.example.renterandroidapp.model.TourAddModel
import com.google.firebase.database.*


class HomeFragments : Fragment() {

    lateinit var recyclerViewAdds : RecyclerView
    lateinit var recyclerViewFeature : RecyclerView

    lateinit var dashboardAdapterCategory: DashboardAdapterCategory
    lateinit var dashboardAdapterTour: DashboardAdapterTour
    lateinit var adapterCategory: DashboardAdapterCategory
    private lateinit var mList: ArrayList<AddDataModel>
    private lateinit var list: ArrayList<TourAddModel>
    lateinit var myRef: DatabaseReference
    lateinit var myRef2: DatabaseReference
    val database = FirebaseDatabase.getInstance()
    lateinit var search: EditText


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myRef=database.getReference("OwnerAdd")
        search=view.findViewById(R.id.search)
        recyclerViewAdds = view.findViewById(R.id.recyclerViewByCategory)
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
        mList = ArrayList()
        recyclerViewAdds.layoutManager=
            NpaLinearLayoutManager(requireContext(), LinearLayout.HORIZONTAL,false)


        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapShot1 : DataSnapshot  in snapshot.children ) {
                    snapShot1.children.map { dataSnapshot ->
//                        dataSnapshot.getValue(AddDataModel::class.java)!!
                        val addDataModel : AddDataModel = dataSnapshot.getValue(AddDataModel::class.java)!!
                        mList.add(addDataModel)
                    } as ArrayList<*>
                }
                dashboardAdapterCategory=DashboardAdapterCategory(mList,requireContext())
                recyclerViewAdds.adapter=dashboardAdapterCategory

            }

            override fun onCancelled(error: DatabaseError) {
            }
        } )

    }
    fun updateRecyclerListWithFilter(data: String?) {
        if (dashboardAdapterCategory != null) {
            dashboardAdapterCategory.filter?.filter(data)
            dashboardAdapterCategory.notifyDataSetChanged()
        }
    }

    private fun setRecyclerViewFeature() {
        list.clear()
        myRef2=database.reference.child("TourAdd")
        myRef2.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapShot1 : DataSnapshot  in snapshot.children ) {
                    snapShot1.children.map { dataSnapshot ->
//                        dataSnapshot.getValue(AddDataModel::class.java)!!
                        val tour : TourAddModel = dataSnapshot.getValue(TourAddModel::class.java)!!
                        list.add(tour)
                    } as ArrayList<*>
                }
                dashboardAdapterTour=DashboardAdapterTour(list,requireContext())
                recyclerViewFeature.adapter=dashboardAdapterTour
            }

            override fun onCancelled(error: DatabaseError) {
            }
        } )

    }

    private fun initView(view: View) {


        recyclerViewFeature = view.findViewById(R.id.recyclerViewFeature)
        list = ArrayList()
        recyclerViewFeature.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)



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