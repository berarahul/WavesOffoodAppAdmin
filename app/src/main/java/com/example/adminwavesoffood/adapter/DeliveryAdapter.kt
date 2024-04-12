package com.example.adminwavesoffood.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwavesoffood.databinding.DelevaryitemBinding

class DeliveryAdapter
    (private val customerNames:MutableList<String>,
     private val moneyStatus:MutableList<Boolean>
    ): RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
       val binding=DelevaryitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }



    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
       holder.bind(position)
    }
    override fun getItemCount(): Int =customerNames.size


    inner  class DeliveryViewHolder(private val binding: DelevaryitemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {
                customername.text=customerNames[position]
                if (moneyStatus[position]== true){
                    notrecived.text="received"
                }else{
                    notrecived.text="notReceived"
                }

                val colormap= mapOf(
                    true to Color.GREEN,
                    false to Color.RED
                )
                notrecived.setTextColor(colormap[moneyStatus[position]]?:Color.BLACK)
                statuscolor.backgroundTintList= ColorStateList.valueOf(colormap[moneyStatus[position]]?:Color.BLACK)
            }
        }
    }

}