import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood.Model.AllMenu
import com.example.adminwavesoffood.databinding.ItemItemBinding
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListner:(position :Int)->Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    private val itemquantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                val quanttity=itemquantities[position]
                val menuItem:AllMenu=menuList[position]
                val uriString:String?=menuItem.foodimage
                val uri: Uri  =Uri.parse(uriString)
                customernamee.text = menuItem.foodname
                foodquantity.text = menuItem.foodprice
                Glide.with(context).load(uri).into(foodimage)

                quantitiy.text=quanttity.toString()
                minusbutton.setOnClickListener {
                    decreasequantity(position)
                }
                plusbutton.setOnClickListener {
                    increasequantity(position)
                }
                deletebutton.setOnClickListener {
                    onDeleteClickListner(position)
                }
            }
        }

        private fun increasequantity(position: Int) {
            if (itemquantities[position] < 10) {
                itemquantities[position]++
                binding.quantitiy.text = itemquantities[position].toString()
            }
        }

        private fun decreasequantity(position: Int) {
            if (itemquantities[position] > 1) {
                itemquantities[position]--
                binding.quantitiy.text = itemquantities[position].toString()

            }
        }

        private fun deleteitem(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
        notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)

        }
    }
}

