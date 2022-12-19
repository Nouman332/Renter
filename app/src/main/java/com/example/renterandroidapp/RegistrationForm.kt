package com.example.renterandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import com.example.renterandroidapp.model.ModelServiceProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RegistrationForm : AppCompatActivity(),View.OnClickListener {

    lateinit var firebaseauthentication: FirebaseAuth
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCnic: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var tilPhone: TextInputLayout
    private lateinit var btnRegister: TextView
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String
    private lateinit var confirmPassword: String
    private lateinit var Cnic: String
    private lateinit var currentDate: String

    val database = FirebaseDatabase.getInstance()
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_registration_form)
        initView()
        setViewListeners()



        firebaseauthentication = FirebaseAuth.getInstance()  //firebase variable initialize
        myRef = database.getReference("ServiceProviderAccount")

        currentDate =
            SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

        initUI()

    }

    private fun isValid(): Boolean {
        if (tilName.editText?.text.isNullOrEmpty()) {
            tilName.editText?.setError("**Enter Name**")
            return false
        }
        if (tilCnic.editText?.text.isNullOrEmpty()) {
            tilCnic.editText?.setError("**Enter CNIC**")

            return false
        } else if (!isValidCnic()) {
            tilCnic.editText?.setError("CNIC Valid Format is *****-*******-*")

            return false


        }
        if (tilPhone.editText?.text.isNullOrEmpty()) {
            tilPhone.editText?.setError("**Enter Phone Number**")

            return false
        } else if (!isValidPhone()) {
            tilPhone.editText?.setError("Phone Valid Format is ****-*******")

            return false


        }
        if (tilEmail.editText?.text.isNullOrEmpty()) {
            tilEmail.editText?.setError("**Enter Email**")
            return false
        } else if (!isValidEmail()) {
            tilEmail.editText?.setError("Email Valid Format is *****@gamil.com")
        }
        if (tilPassword.editText?.text.isNullOrEmpty()) {
            tilPassword.editText?.setError("**Enter Password**")
            return false
        } else if (!isValidPassword()) {
            tilPassword.editText?.setError("Enter Strong Password")
            return false
        }
        if (tilConfirmPassword.editText?.text.isNullOrEmpty()) {
            tilConfirmPassword.editText?.setError("**Missing Password**")
            return false
        } else if (!isValidConfirmPassword()) {
            tilConfirmPassword.editText?.setError("Confirm Password Mismatched ")
            return false
        }





        return true
    }

    private fun isValidCnic(): Boolean {
        val pattern = Regex("^[0-9]{5}-[0-9]{7}-[0-9]$")
        return pattern.containsMatchIn(tilCnic.editText?.text.toString())

    }

    private fun isValidEmail(): Boolean {
        val pattern = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
        return pattern.containsMatchIn(tilEmail.editText?.text.toString())

    }

    private fun isValidPassword(): Boolean {
        val pattern =
            Regex("(?=^.{8,}\$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*\$")
        return pattern.containsMatchIn(tilPassword.editText?.text.toString())

    }

    private fun isValidPhone(): Boolean {
        val pattern =
            Regex("^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}\$|^\\d{11}\$|^\\d{4}-\\d{7}\$")
        return pattern.containsMatchIn(tilPhone.editText?.text.toString())

    }

    private fun isValidConfirmPassword(): Boolean {
        var password: String
        var confirmPassword: String
        password = tilPassword.editText?.text.toString()
        confirmPassword = tilConfirmPassword.editText?.text.toString()
        //return pattern.containsMatchIn(tilPassword.editText?.text.toString())
        return password == confirmPassword
    }

    private fun initView() {
        tilName = findViewById(R.id.tilName)
        tilCnic = findViewById(R.id.tilCnic)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilpassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tilPhone = findViewById(R.id.tilPhone)


    }

    private fun setViewListeners() {

        btnRegister.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnRegister -> {
                if (isValid()) {
                    name = tilName.editText?.text.toString()
                    email = tilEmail.editText?.text.toString()
                    password = tilPassword.editText?.text.toString()
                    confirmPassword = tilConfirmPassword.editText?.text.toString()
                    Cnic = tilCnic.editText?.text.toString()
                    phone = tilPhone.editText?.text.toString()


                    sendDataForAuthAndRealTimeDataBase(
                        name,
                        Cnic,
                        email,
                        password,
                        confirmPassword,
                        phone,
                        currentDate
                    )


                }


            }

        }
    }

    private fun sendDataForAuthAndRealTimeDataBase(
        name: String,
        cnic: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String,
        currentDate: String
    ) {
        firebaseauthentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    myRef.child(firebaseauthentication.uid.toString()).setValue(
                        ModelServiceProvider(
                            firebaseauthentication.currentUser?.uid.toString(),
                            name,
                            cnic,
                            email,
                            phone,
                            "",
                            false,
                            joinDate = currentDate,
                            "",
                            "",
                            "",
                            "",
                            gender = "",
                            onlineStatus = false
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            var intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@RegistrationForm,
                                it.exception?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                } else
                    Toast.makeText(this@RegistrationForm, it.exception?.message, Toast.LENGTH_LONG)
                        .show()

            }

    }


    private fun initUI() {
        //UI reference of textView

        // customerAutoTV.setAdapter(adapter)
        //submit button click event registration

    }


}
