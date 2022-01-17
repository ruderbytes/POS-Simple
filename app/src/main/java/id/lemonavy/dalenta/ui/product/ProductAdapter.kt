package id.lemonavy.dalenta.ui.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup 
import androidx.recyclerview.widget.RecyclerView
import id.lemonavy.dalenta.R
import id.lemonavy.dalenta.db.entity.ItemData
import kotlinx.android.synthetic.main.item_product.view.*
import java.text.DecimalFormat

class ProductAdapter(private var listAccount: MutableList<ItemData>,
                     private val listener: ItemClickListener)
    : RecyclerView.Adapter<ProductAdapterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ProductAdapterHolder {
        val itemContainer = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false) as ViewGroup
        val viewHolder = ProductAdapterHolder(itemContainer)

        itemContainer.setOnClickListener {

        }

        return viewHolder
    }

    override fun getItemCount(): Int = listAccount.size

    override fun onBindViewHolder(data: ProductAdapterHolder, position: Int) {
        val itemList = listAccount[position]
        data.let {
            it.bind(itemList)

            it.itemView.setOnClickListener {
                listener.onClick(itemList)
            }
        }
    }

}

class ProductAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    var moneyFormat = DecimalFormat("#,###,###")
    private val ivImage = itemView.itemImage!!
    private val tvTitle = itemView.itemTitle!!
    private val tvPrice = itemView.itemPrice!!

    fun bind(cart: ItemData) {
        ivImage.setImageResource(R.drawable.sample)
        tvTitle.text = cart.itemName
        tvPrice.text = "Rp. " + moneyFormat.format(cart.itemPrice.toLong())
    }
}

interface ItemClickListener {
    fun onClick(data: ItemData)
}