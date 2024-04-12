package com.example.adminwavesoffood

import MenuItemAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.Model.AllMenu
import com.example.adminwavesoffood.databinding.ActivityAllitemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllitemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems:ArrayList<AllMenu> = ArrayList()


    private val binding:ActivityAllitemBinding by lazy {
        ActivityAllitemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        retrivemenuitems()

        binding.backbutton.setOnClickListener {
            finish()
        }
    }

    private fun retrivemenuitems() {

        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")

        //fetch from database
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear Existing data before populating
                menuItems.clear()
                //loop for through each food item
                for (foodSnapshot:DataSnapshot in snapshot.children){

                    val menuItem:AllMenu?=foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
             Log.d("databaseerror","error ${error.message}")
            }
        })

    }

    private fun setAdapter() {

        val adapter=MenuItemAdapter(this@AllitemActivity,menuItems,databaseReference){position ->
            deleteMenuItem(position)
        }
        binding.menuRecyclerview.layoutManager=LinearLayoutManager(this)
        binding.menuRecyclerview.adapter=adapter


    }

    private fun deleteMenuItem(position: Int) {
        val menuitemToDelete:AllMenu=menuItems[position]
        val menuItemKey:String?=menuitemToDelete.key
        val foodmenuReference:DatabaseReference=database.reference.child("menu").child(menuItemKey!!)
        foodmenuReference.removeValue().addOnCompleteListener { task ->

            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.menuRecyclerview.adapter?.notifyItemRemoved(position)
            }
            else{
                Toast.makeText(this,"Item Not Deleted",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
