package com.example.adminwavesoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.Model.OrderDetails
import com.example.adminwavesoffood.adapter.orderDetilsAdapter
import com.example.adminwavesoffood.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    private val binding:ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName:String?=null
    private var Address :String?=null
    private var phoneNumber:String?=null
    private var totalprice:String?=null
    private  var foodNames:ArrayList<String> = arrayListOf()
    private  var foodImages:ArrayList<String>  = arrayListOf()
    private  var foodQuantities:ArrayList<Int>  = arrayListOf()
    private  var foodPrices:ArrayList<String>  = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.payoutbackbutton.setOnClickListener {
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
       val recevedOrderDetails: OrderDetails =intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        recevedOrderDetails?.let { orderDetails ->

            userName = recevedOrderDetails.userName
            foodNames = recevedOrderDetails.foodNames as ArrayList<String>
            foodImages = recevedOrderDetails.foodImages as ArrayList<String>
            foodQuantities = recevedOrderDetails.foodQuantities as ArrayList<Int>
            Address = recevedOrderDetails.address
            phoneNumber = recevedOrderDetails.phoneNumber
            foodPrices = recevedOrderDetails.foodprices as ArrayList<String>
            totalprice = recevedOrderDetails.totalPrice

            setUserDetails()
            setAdapter()
        }

    }


    private fun setUserDetails() {

        binding.name.text=userName
        binding.address.text=Address
        binding.phonenumber.text=phoneNumber
        binding.totalamount.text=totalprice

    }

    private fun setAdapter() {

        binding.orderDetailsRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=orderDetilsAdapter(this,foodNames,foodImages,foodQuantities,foodPrices)
        binding.orderDetailsRecyclerView.adapter=adapter
    }
}