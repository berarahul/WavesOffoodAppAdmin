package com.example.adminwavesoffood

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminwavesoffood.Model.AllMenu
import com.example.adminwavesoffood.databinding.ActivityAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddItemActivity : AppCompatActivity() {
    //Food item Details
    private lateinit var foodname:String
    private lateinit var foodprice:String
    private lateinit var fooddescription:String

    private lateinit var foodingradient:String
    private var foodimageuri:Uri?=null
    //for firebase
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase


    private lateinit var binding: ActivityAddItemBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initialize Firebase
        auth=FirebaseAuth.getInstance()
        // initialize firebase database instance
        database=FirebaseDatabase.getInstance()

binding.additembutton.setOnClickListener {
    //get data from field

    foodname=binding.enterfoodname.text.toString().trim()
    foodprice=binding.enterfoodprice.text.toString().trim()
    fooddescription=binding.description.text.toString().trim()
  foodingradient  =binding.ingradients.text.toString().trim()
    if (!(foodname.isBlank()||foodprice.isBlank()||fooddescription.isBlank()||foodingradient.isBlank())){
        uploaddata()
        Toast.makeText(this,"Item Add SuccessFully",Toast.LENGTH_SHORT).show()
        finish()
    }
    else
    {
        Toast.makeText(this,"Fill The All Details",Toast.LENGTH_SHORT).show()
    }
}

        binding.selectimage.setOnClickListener {
            pickImage.launch("image/*")

        }
        binding.backbutton.setOnClickListener{
            finish()
        }
    }

    private fun uploaddata() {
       //get a references to the menu node in the database

        val menuRef:DatabaseReference=database.getReference("menu")
        //Generate a unique key for the new menu item
        val newItemKey:String?=menuRef.push().key
        if (foodimageuri!=null){
            val storageRef:StorageReference =FirebaseStorage.getInstance().reference
            val imageref:StorageReference=storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadtask: UploadTask =imageref.putFile(foodimageuri!!)



            uploadtask.addOnSuccessListener {
                imageref.downloadUrl.addOnSuccessListener {
                    downloadurl->
                    //create a new menu item
                    val newitem=AllMenu(
                        newItemKey,
                        foodname=foodname,
                        foodprice=foodprice,
                        fooddescription=fooddescription,
                      foodingradiants = foodingradient,
                        foodimage = downloadurl.toString(),

                    )
                    newItemKey?.let{
                        key->
                        menuRef.child(key).setValue(newitem).addOnSuccessListener {
                            Toast.makeText(this,"Data Upload SuccessFully",Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this,"Data Upload Failed",Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            } .addOnFailureListener{
                Toast.makeText(this,"Image Upload Failed",Toast.LENGTH_SHORT).show()
            }

        }
          else{
                Toast.makeText(this,"Please Select An Image",Toast.LENGTH_SHORT).show()
            }
    }


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedimage.setImageURI(uri)
            foodimageuri=uri
        }
    }
}
