package id.lemonavy.dalenta.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup 
import androidx.recyclerview.widget.RecyclerView
import id.lemonavy.dalenta.R
import id.lemonavy.dalenta.db.entity.CartData
import kotlinx.android.synthetic.main.item_cart.view.*
import java.text.DecimalFormat

class CartAdapter(private var listAccount: MutableList<CartData>,
                     private val listener: ItemClickListener)
    : RecyclerView.Adapter<ProductAdapterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ProductAdapterHolder {
        val itemContainer = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false) as ViewGroup
        val viewHolder = ProductAdapterHolder(itemContainer)

        itemContainer.setOnClickListener {

        }

        return viewHolder
    }

    override fun getItemCount(): Int = listAccount.size
    private var subTotal = 0
    override fun onBindViewHolder(data: ProductAdapterHolder, position: Int) {
        val itemList = listAccount[position]
        data.let {
            it.bind(itemList)

            val priceByQty = (itemList.itemPrice +itemList.itemPriceTopping) * itemList.itemQty.toInt()
            subTotal += priceByQty

            val taxServ = if(itemList.taxService){
                5
            }else{
                0
            }
            val taxGst = if(itemList.taxGst){
                10
            }else{
                0
            }
            val taxServTotal = subTotal * taxServ / 100
            val taxGstTotal = subTotal * taxGst / 100

            val taxesTotal = taxServTotal + taxGstTotal

            listener.onInitTotal(subTotal, taxesTotal)

            it.itemView.setOnClickListener {
                listener.onClick(itemList)
            }
        }
    }

}

class ProductAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    var moneyFormat = DecimalFormat("#,###,###")
    private val tvTitle = itemView.itemTitle!!
    private val tvPrice = itemView.itemPrice!!
    private val tvPortion = itemView.itemPortion!!

    fun bind(cart: CartData) {
        var qtyLabel = ""
        if(cart.itemQty.toInt()>1){
            qtyLabel=" x"+cart.itemQty
        }
        tvTitle.text = cart.itemName + qtyLabel
        tvPortion.text = cart.itemSize

        val priceTotal = (cart.itemPrice + cart.itemPriceTopping) * cart.itemQty.toInt()

        tvPrice.text = "Rp. " + moneyFormat.format(priceTotal.toLong())
    }
}

interface ItemClickListener {
    fun onClick(data: CartData)
    fun onInitTotal(subTotal: Int, taxes: Int)
}