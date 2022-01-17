package id.lemonavy.dalenta.repository
  
import androidx.lifecycle.LiveData
import id.lemonavy.dalenta.db.dao.ProductDao
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.db.entity.ItemData

class ItemRepository (val db: ProductDao){

    fun getItems(): LiveData<MutableList<ItemData>> = db.getItems()

    fun getItemId(id: Long): CartData = db.getItemByUnique(id)

    fun getCart(): LiveData<MutableList<CartData>> = db.getCart()

    fun updateCartById(id: String,
                       size: String, quantity: String,
                       price: Int, topping: Int, notes: String,
                       service: Boolean, gst: Boolean) = db.updateCartById(
        id, size, quantity, price, topping, notes, service, gst
    )
    fun insertCart(item: CartData) = db.insertCart(item)
}