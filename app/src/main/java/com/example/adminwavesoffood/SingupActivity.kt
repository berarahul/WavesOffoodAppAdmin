package com.example.adminwavesoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.adminwavesoffood.Model.UserModel
import com.example.adminwavesoffood.databinding.ActivitySingupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SingupActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var nameofresturant:String
    private lateinit var databse:DatabaseReference


    private  val binding:ActivitySingupBinding by lazy {
        ActivitySingupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialize FireBase Auth
        auth=Firebase.auth
        //Initialize Firebase Database
        databse=Firebase.database.reference



        binding.CreateUserButton.setOnClickListener {
            //get text from edit text
            username=binding.NameOfOwner.text.toString().trim()
           nameofresturant=binding.NameOfResturant.text.toString().trim()
            email=binding.Email.text.toString().trim()
            password=binding.passWord.text.toString().trim()
           if (username.isBlank()||nameofresturant.isBlank()||email.isBlank()||password.isBlank()){
               Toast.makeText(this,"please fill all Details",Toast.LENGTH_SHORT).show()
           }else{
               createAccount(email,password)
           }



        }
        binding.donthaveaccountforsingup.setOnClickListener{

            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)

        }

     val locationlist= arrayOf("jaipur","odisha","Maharastra","Mumbai")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,locationlist)
        val autocompletetextview=binding.listoflocation
        autocompletetextview.setAdapter(adapter)

    }

    private fun createAccount(email: String, password: String) {
           auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->

               if (task.isSuccessful){
                   Toast.makeText(this,"Account created Successfully",Toast.LENGTH_SHORT).show()
                   SaveUserData()
                   val intent=Intent(this,LoginActivity::class.java)
                   startActivity(intent)
                   finish()
               }else
               {
                   Toast.makeText(this,"Account creation Failed",Toast.LENGTH_SHORT).show()
                   Log.d("Account","CreateAccount: failure",task.exception)
               }

           }
    }
//save data in to database
    private fun SaveUserData() {
        username=binding.NameOfOwner.text.toString().trim()
        nameofresturant=binding.NameOfResturant.text.toString().trim()
        email=binding.Email.text.toString().trim()
        password=binding.passWord.text.toString().trim()
        val user=UserModel(username,nameofresturant,email,password)
        val UserId: String =FirebaseAuth.getInstance().currentUser!!.uid
    //save user data Firebase Database
        databse.child("user").child(UserId).setValue(user)
    }
}