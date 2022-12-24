package com.example.renterandroidapp.dashboard.fragments
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.renterandroidapp.Login
import com.example.renterandroidapp.R
import com.example.renterandroidapp.dashboard.HomePage
import com.example.renterandroidapp.sharedpreference.SharedPref
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {
    lateinit var userid: String
    private lateinit var firebaseAuthentication: FirebaseAuth
    lateinit var myRef: DatabaseReference
    private val database = FirebaseDatabase.getInstance()
    private lateinit var progressBar: ProgressBar
    lateinit var name : EditText
    lateinit var phone : EditText
    lateinit var email : EditText
    lateinit var userName : String
    lateinit var userEmail: String
    lateinit var  userPhone : String
    lateinit var editUser: TextView
    private lateinit var logOutUser: TextView
    private lateinit var userNameMian: TextView
    private lateinit var complain: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializations(view)
        getUserData()
    }

    fun getUserData(){
        progressBar.visibility= View.VISIBLE
        userid= firebaseAuthentication.currentUser?.uid.toString()
        myRef=database.getReference("Users").child("RenterAccount").child(userid)
        myRef.addValueEventListener(object: ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
                userName = snapshot.child("name").getValue(String::class.java).toString()
                userPhone = snapshot.child("phone").getValue(String::class.java).toString()
                userEmail = snapshot.child("email").getValue(String::class.java).toString()

                name.setText(userName)
                phone.setText(userPhone)
                email.setText(userEmail)
                userNameMian.text=userName.toString()
                progressBar.visibility= View.INVISIBLE
            }
            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility= View.INVISIBLE
            }
        } )
    }
    fun initializations(view: View){
        firebaseAuthentication = FirebaseAuth.getInstance()  //firebase variable initialize
        progressBar=view.findViewById(R.id.prog)
        name=view.findViewById(R.id.userName)
        phone=view.findViewById(R.id.userPhone)
        email=view.findViewById(R.id.userEmail)
        editUser=view.findViewById(R.id.btnEdit)
        logOutUser=view.findViewById(R.id.btnLogout)
        complain=view.findViewById(R.id.btnComplain)
        userNameMian=view.findViewById(R.id.UserNameMain)

        userid= firebaseAuthentication.currentUser?.uid.toString()
        name.isFocusable = false
        name.isClickable = false
        phone.isFocusable = false
        phone.isClickable = false
        email.isFocusable = false
        email.isClickable = false

        complain.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("rentalestate23@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Complaint")
            startActivity(Intent.createChooser(intent, "Email via..."))

        }

        editUser.setOnClickListener {
            if (editUser.text.equals("Save")){
                logOutUser.visibility = View.VISIBLE
                userid= firebaseAuthentication.currentUser?.uid.toString()
                val result: HashMap<String, Any> = HashMap()
                result.put("name",name.text.toString())
                result.put("email",email.text.toString())
                result.put("phone",phone.text.toString())
                myRef=database.getReference().child("Users").child("RenterAccount")

                val query : Task<Void> = myRef.child(userid).updateChildren(result)
                query.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        name.isFocusable = false
                        name.isClickable = false
                        phone.isFocusable = false
                        phone.isClickable = false
                        email.isFocusable = false
                        email.isClickable = false
                        editUser.text="Edit"
//                        activity?.toast("Info Updated Successfully")
                        //enter your code what you want excute                     after remove value in firebase.
                    } else {
                        //enter msg or enter your code which you want to show in case of value is not remove properly or removed failed.
//                        activity?.toast("Info Update Failed")
                    }
                }

            }
            else {
                name.isFocusableInTouchMode = true
                name.isClickable = true
                phone.isFocusableInTouchMode = true
                phone.isClickable = true
                email.isFocusableInTouchMode = true
                email.isClickable = true
                editUser.text = "Save"
                logOutUser.visibility = View.INVISIBLE
            }
        }
        logOutUser.setOnClickListener {
            SharedPref.write("isLoggedIn",false)
            val intent = Intent(activity as HomePage , Login::class.java)
            startActivity(intent)
            (activity as HomePage).finish()
        }

    }

}