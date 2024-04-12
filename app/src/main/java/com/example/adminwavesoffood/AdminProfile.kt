package com.example.adminwavesoffood

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminwavesoffood.Model.UserModel
import com.example.adminwavesoffood.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfile : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference:DatabaseReference

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        adminReference=database.reference.child("user")

        binding.backbutton.setOnClickListener {
            finish()
        }
        binding.saveinfo.setOnClickListener {
            updateUserData()

        }
        binding.name.isEnabled=false
        binding.address.isEnabled=false
        binding.email.isEnabled=false
        binding.phonenumber.isEnabled=false
        binding.password.isEnabled=false
        binding.saveinfo.isEnabled=false
      var isenable=false
        binding.edittextbutton.setOnClickListener{
            isenable=!isenable
            binding.name.isEnabled=isenable
            binding.address.isEnabled=isenable
            binding.email.isEnabled=isenable
            binding.phonenumber.isEnabled=isenable
            binding.password.isEnabled=isenable
            if (isenable){
                binding.name.requestFocus()
            }
            binding.saveinfo.isEnabled=isenable
        }
        retriveuserData()
    }



    private fun retriveuserData() {

        val currentuserId:String?=auth.currentUser?.uid
        if (currentuserId!= null){

            val userReference:DatabaseReference=adminReference.child(currentuserId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var ownerName:Any?=snapshot.child("name").getValue()
                        var ownerEmail:Any?=snapshot.child("email").getValue()
                        var ownerPassword:Any?=snapshot.child("password").getValue()
                        var ownerAddress:Any?=snapshot.child("address").getValue()
                        var ownerPhoneNumber:Any?=snapshot.child("phone").getValue()
                        setDataToTextView(ownerName,ownerEmail,ownerPassword,ownerAddress,ownerPhoneNumber)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        ownerEmail: Any?,
        ownerPassword: Any?,
        ownerAddress: Any?,
        ownerPhoneNumber: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(ownerEmail.toString())
        binding.password.setText(ownerPassword.toString())
        binding.address.setText(ownerAddress.toString())
        binding.phonenumber.setText(ownerPhoneNumber.toString())

    }
    private fun updateUserData() {

        val updateName =   binding.name.text.toString()
     var updateEmail=   binding.email.text.toString()
     var updatePassword=   binding.password.text.toString()
        var updatePhone=   binding.phonenumber.text.toString()
      var updateaddress=  binding.address.text.toString()
     val currentUserUid:String?=auth.currentUser?.uid
        if (currentUserUid!=null){
            val userRefernce:DatabaseReference=adminReference.child(currentUserUid)

userRefernce.child("name").setValue(updateName)
userRefernce.child("email").setValue(updateEmail)
userRefernce.child("password").setValue(updatePassword)
userRefernce.child("phone").setValue(updatePhone)
userRefernce.child("address").setValue(updateaddress)

            Toast.makeText(this,"Profile Updated Success full",Toast.LENGTH_SHORT).show()
            // update the email and password firebase authentication
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }


        else{
            Toast.makeText(this,"Profile Updated Failed",Toast.LENGTH_SHORT).show()
        }

    }
}