package com.example.adminwavesoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.Model.OrderDetails
import com.example.adminwavesoffood.adapter.PendingOrderAdapter
import com.example.adminwavesoffood.databinding.ActivityPendingOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class PendingOrderActivity : AppCompatActivity() ,PendingOrderAdapter.OnItemClicked{
   private lateinit var binding: ActivityPendingOrderBinding
   private var listofName:MutableList<String> = mutableListOf()
   private var listoftotalPrice:MutableList<String> = mutableListOf()
   private var listofImageFirstFoodOrder:MutableList<String> = mutableListOf()
   private var listofOrderItem:ArrayList<OrderDetails> = arrayListOf()
private lateinit var database: FirebaseDatabase
private lateinit var databaseOrderDetails: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
//initialization of database
        database=FirebaseDatabase.getInstance()
        //initialization of database reference
        databaseOrderDetails=database.reference.child("orderDetails")
        getordersDetails()
        binding.backbutton.setOnClickListener {
            finish()
        }

    }

    private fun getordersDetails() {
        //retrive order details from firebase database

        databaseOrderDetails.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ordersnapshot in snapshot.children){

                    val orderDetails=ordersnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listofOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addDataToListForRecyclerView() {
       for (orderitem in listofOrderItem){

           // add data to respective list for populating the recyclerview

           orderitem.userName?.let { listofName.add(it) }
           orderitem.totalPrice?.let { listoftotalPrice.add(it) }
           orderitem.foodImages?.filterNot { it.isEmpty() }?.forEach{
               listofImageFirstFoodOrder.add(it)
           }

       }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingorderRecyclerview.layoutManager=LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this,listofName,listoftotalPrice,listofImageFirstFoodOrder,this)
        binding.pendingorderRecyclerview.adapter=adapter
    }

    override fun OnItemClickListener(position: Int) {
    val intent =Intent(this,OrderDetailsActivity::class.java)
        val userOrderDetails:OrderDetails=listofOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun OnItemAcceptClickListener(position: Int) {
        //Handle Item Acceptance and update database

        val childitempushkey:String?= listofOrderItem[position].itemPushKey
        val clickorderItemReference:DatabaseReference?=childitempushkey?.let {
            database.reference.child("orderDetails").child(it)
        }
        clickorderItemReference?.child("orderAccepted")?.setValue(true)
        upDataOrderAceeptStatus(position)
    }


    override fun OnItemDispatchClickListener(position: Int) {
        val dispatchItemPushkey:String?=listofOrderItem[position].itemPushKey
        val dispatchItemOrderreference:DatabaseReference=database.reference.child("CompletedOrder").child(dispatchItemPushkey!!)
        dispatchItemOrderreference.setValue(listofOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemOrderDetails(dispatchItemPushkey)
            }
    }

    private fun deleteThisItemOrderDetails(dispatchItemPushkey: String) {
        val orderDetailsitemreference:DatabaseReference=database.reference.child("orderDetails").child(dispatchItemPushkey)
        orderDetailsitemreference.removeValue().addOnSuccessListener {
            Toast.makeText(this,"Order is Dispatched",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{

            Toast.makeText(this,"Order is Not Dispatched",Toast.LENGTH_SHORT).show()

        }
    }

    private fun upDataOrderAceeptStatus(position: Int) {
     //item order acceptance in user Buy History and orderDetails
        val userIdofClickItem:String?=listofOrderItem[position].userUid
        val pushkeyofClickedItem:String?=listofOrderItem[position].itemPushKey
        val buyHistoryReference:DatabaseReference=database.reference.child("user").child(userIdofClickItem!!).child("BuyHistory").child(pushkeyofClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushkeyofClickedItem).child("orderAccepted").setValue(true)
    }
}