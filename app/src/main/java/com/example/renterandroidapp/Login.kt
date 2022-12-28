package com.example.renterandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.renterandroidapp.dashboard.HomePage
import com.example.renterandroidapp.sharedpreference.SharedPref
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Login : AppCompatActivity() {

    lateinit var firebaseauthentication: FirebaseAuth
    lateinit var txtForgetPassword: TextView
    lateinit var myRef: DatabaseReference
    val database = FirebaseDatabase.getInstance()
    lateinit var email: TextInputLayout
    lateinit var password: TextInputLayout
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        progressBar=findViewById(R.id.progress)
         email = findViewById(R.id.emailid)
         password= findViewById(R.id.passwordName)
        firebaseauthentication = FirebaseAuth.getInstance()  ////firebase variable initialize
        myRef = database.getReference("Users").child("RenterAccount")

//        isAlreadyLogin()
        //button listner
        var registerbutton: TextView = findViewById(R.id.registerbtn)
        registerbutton.setOnClickListener {
            val intent = Intent(this, RegistrationForm::class.java)
            startActivity(intent)

        }

        var loginbutton: TextView = findViewById(R.id.loginbtn)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        loginbutton.setOnClickListener {

//            startActivity(Intent(this@Login, HomePage::class.java))
            progressBar.visibility= View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            if (isValid()){
                val email : String  = email.editText?.text.toString()
                val pass : String  = password.editText?.text.toString()
                firebaseauthentication.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            var userid= firebaseauthentication.currentUser?.uid.toString()
                            myRef=database.getReference("Users").child("RenterAccount").child(userid)
                            myRef.addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val isApproved: String = snapshot.child("approved").getValue(String::class.java).toString()
                                    if (isApproved=="true"){
                                        SharedPref.write("isLoggedInRenter",true)
                                        val intent = Intent(this@Login, HomePage::class.java)
                                        startActivity(intent)
                                        finish()
                                        progressBar.visibility= View.INVISIBLE
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                    }
                                    else{
                                        progressBar.visibility= View.INVISIBLE
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                        toast("Your Account is Not yet approved by Admin...")

                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    toast(error.message.toString())
                                    progressBar.visibility= View.INVISIBLE
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                }
                            } )

                        }
                        else
                        {
                            progressBar.visibility= View.INVISIBLE
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            toast(it.exception?.message.toString())

                        }
                    }


            }
            else
                progressBar.visibility= View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)


        }
        txtForgetPassword.setOnClickListener {

            if (email.editText?.text.toString().isEmpty()) {
                email.error = "Email is required.";

            } else {
                firebaseauthentication.sendPasswordResetEmail(email.editText?.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            toast("Recovery link has been sent to your email address.")
                        } else {
                            toast(it.exception.toString())
                        }

                    }
            }
        }
    }

    private fun isAlreadyLogin() {
        if( firebaseauthentication.currentUser!= null  )
        {
            myRef.child(firebaseauthentication.currentUser?.uid.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isApproved= snapshot.child("approved").getValue() as Boolean
                    if (isApproved){
                        val intent = Intent(this@Login, HomePage::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        toast("Your Account is Not approved by Admin yet!! Please Wait...")
                        firebaseauthentication.signOut()
                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        }
    }
    fun isValid() : Boolean {
        if (email.editText?.text.toString().isEmpty()) {
            email.error = "Email is required."
            return false
        }
        if (password.editText?.text.toString().isEmpty()) {
            password.error = "Password is required."

            return false
        }

        return true

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        finishAffinity()
    }
}

