package com.example.adminwavesoffood

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminwavesoffood.Model.OrderDetails
import com.example.adminwavesoffood.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()



        binding.addmenu.setOnClickListener{
            val intent= Intent(this,AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allmenuitem.setOnClickListener{
            val intent= Intent(this,AllitemActivity::class.java)
            startActivity(intent)
        }
        binding.outfordelivery.setOnClickListener{
            val intent= Intent(this,OutForDelivary::class.java)
            startActivity(intent)
        }
        binding.profilee.setOnClickListener{
            val intent= Intent(this,AdminProfile::class.java)
            startActivity(intent)
        }
        binding.createuser.setOnClickListener{
            val intent= Intent(this,CreateUser::class.java)
            startActivity(intent)
        }
        binding.PendingorderTextView.setOnClickListener{
            val intent= Intent(this,PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logoutbutton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,SingupActivity::class.java))
            finish()
        }
        pendingOrders()
        completedOrders()
        wholeTimeEarning()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun wholeTimeEarning() {
        var listoftotalPay = mutableListOf<Int>()

      completedOrderReference=FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              for (orderSnapshot in snapshot.children){
                  var completeOrder : OrderDetails?=orderSnapshot.getValue(OrderDetails::class.java)
                  completeOrder?.totalPrice?.replace("$","")?.toIntOrNull()
                      ?.let { i ->

                          listoftotalPay.add(i)
                      }

              }
                binding.wholetimeEarning.text=listoftotalPay.sum().toString()+"$"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun completedOrders() {
        var CompletedorderReference:DatabaseReference=database.reference.child("CompletedOrder")
        var CompleteorderitemCount=0
        CompletedorderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                CompleteorderitemCount=snapshot.childrenCount.toInt()
                binding.completeOrder.text=CompleteorderitemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun pendingOrders() {

        database=FirebaseDatabase.getInstance()
        var pendingorderReference:DatabaseReference=database.reference.child("orderDetails")
        var pendingorderitemCount=0
        pendingorderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               pendingorderitemCount=snapshot.childrenCount.toInt()
               binding.pendingOrder.text=pendingorderitemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

