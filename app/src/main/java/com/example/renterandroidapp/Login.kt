package com.example.renterandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.renterandroidapp.dashboard.HomePage
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Login : AppCompatActivity() {

    lateinit var firebaseauthentication: FirebaseAuth
    lateinit var txtForgetPassword: TextView
    lateinit var myRef: DatabaseReference
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        var email: TextInputLayout = findViewById(R.id.emailid)
        var password: TextInputLayout = findViewById(R.id.passwordName)
        firebaseauthentication = FirebaseAuth.getInstance()  ////firebase variable initialize
        myRef = database.getReference("ServiceProviderAccount")

        isAlreadyLogin()
        //button listner
        var registerbutton: TextView = findViewById(R.id.registerbtn)
        registerbutton.setOnClickListener {
            val intent = Intent(this, RegistrationForm::class.java)
            startActivity(intent)

        }

        var loginbutton: TextView = findViewById(R.id.loginbtn)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        loginbutton.setOnClickListener {

            startActivity(Intent(this@Login, HomePage::class.java))

   /*         var emailString = email.editText?.text.toString()// null safety
            var passwordString = password.editText?.text.toString()

            firebaseauthentication.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful)
                    {
                        myRef.child(firebaseauthentication.currentUser?.uid.toString()).addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var isApproved= snapshot.child("approved").value as Boolean
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

                    } else
                        Toast.makeText(this@Login, it.exception?.message, Toast.LENGTH_LONG).show()
                }*/


        }
        txtForgetPassword.setOnClickListener {

            if (email.editText?.text.toString().isEmpty()) {
                email.error = "Email is required.";
                toast("Email jo bhi")

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
}

