package com.example.adminwavesoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.Model.OrderDetails
import com.example.adminwavesoffood.adapter.DeliveryAdapter
import com.example.adminwavesoffood.databinding.ActivityOutForDelivaryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class OutForDelivary : AppCompatActivity() {
    private val binding:ActivityOutForDelivaryBinding by lazy {
        ActivityOutForDelivaryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private  var listofCompleteorderlist:ArrayList<OrderDetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backbutton.setOnClickListener {
            finish()
        }
  //retrive and display completed order

         retriveCompleteorderdetails()

    }

    private fun retriveCompleteorderdetails() {
        //initialize firebase datadase

        database=FirebaseDatabase.getInstance()
        val completeorderReference:Query= database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeorderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the list before populating with new data
                listofCompleteorderlist.clear()

                for (ordersnapshot : DataSnapshot in snapshot.children){

                    val completeorder:OrderDetails?=ordersnapshot.getValue(OrderDetails::class.java)
                    completeorder?.let {
                        listofCompleteorderlist.add(it)
                    }
                }
                // reverse the list to display latest order first
                listofCompleteorderlist.reverse()

                setDataintoRecyclerview()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setDataintoRecyclerview() {
        // initialization list to hold customers name and payment status
        val customerName: MutableList<String> = mutableListOf<String>()
        val moneyStatus:MutableList<Boolean> = mutableListOf<Boolean>()

        for (order in listofCompleteorderlist){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter=DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerview.adapter=adapter
        binding.deliveryRecyclerview.layoutManager=LinearLayoutManager(this)
    }
}