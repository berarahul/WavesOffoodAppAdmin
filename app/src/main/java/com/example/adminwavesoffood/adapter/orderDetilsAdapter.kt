package com.example.adminwavesoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood.databinding.ActivityOrderDetailsBinding
import com.example.adminwavesoffood.databinding.OrderdetailsitemBinding

class orderDetilsAdapter (private val context:Context,

    private var foodname:ArrayList<String>,
    private var foodImage:ArrayList<String>,
    private var foodQuantities:ArrayList<Int>,
    private var foodPrice:ArrayList<String>



    ):RecyclerView.Adapter<orderDetilsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding:OrderdetailsitemBinding=OrderdetailsitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }



    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
     holder.bind(position)
    }
    override fun getItemCount(): Int = foodname.size
    inner  class OrderDetailsViewHolder(private val binding: OrderdetailsitemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodnamee.text=foodname[position]
                foodquantity.text=foodQuantities[position].toString()
                val uriString:String =foodImage[position]
                val uri:Uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderfooditemimage)
                foodprice.text=foodPrice[position]

            }
        }

    }
}