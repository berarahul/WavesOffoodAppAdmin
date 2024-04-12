package com.example.adminwavesoffood.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood.databinding.PendingrderitemBinding
import com.facebook.appevents.codeless.internal.ViewHierarchy.setOnClickListener


class PendingOrderAdapter(
    private val context: Context,
    private val customerNames:MutableList<String>,
    private val customerQuantity:MutableList<String>,
    private val foodImage:MutableList<String>,
   private val itemClicked:OnItemClicked ,
      ):
    RecyclerView.Adapter<PendingOrderAdapter.PendingOederViewHolder>() {
  interface OnItemClicked{

      fun OnItemClickListener(position: Int)
      fun OnItemAcceptClickListener(position: Int)
      fun OnItemDispatchClickListener(position: Int)



  }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOederViewHolder {
    val binding=PendingrderitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOederViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PendingOederViewHolder, position: Int) {
       holder.bind(position)
    }
    override fun getItemCount(): Int =customerNames.size

    inner class PendingOederViewHolder(private val binding: PendingrderitemBinding):RecyclerView.ViewHolder(binding.root) {
        private var isAccepted=false
        fun bind(position: Int) {
            binding.apply {

                customernamee.text=customerNames[position]
                foodquantity.text=customerQuantity[position]
                var uriString = foodImage[position]
                var uri:Uri= Uri.parse(uriString)

               Glide.with(context).load(uri).into(orderfooditemimage)
                foodprice.apply {
                if (!isAccepted) {

                    text="Accept"

                }   else
                {
                    text="Dispatch"
                }
                    setOnClickListener {
                        if (!isAccepted){
                            text="Dispatch"
                            isAccepted=true
                            Toast.makeText(context,"Order is Accepted",Toast.LENGTH_SHORT).show()
                            itemClicked.OnItemAcceptClickListener(position)
                        }else
                        {
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            Toast.makeText(context,"Order is Dispatch",Toast.LENGTH_SHORT).show()
                            itemClicked.OnItemDispatchClickListener(position)

                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.OnItemClickListener(position)
                }
            }


        }


    }
}