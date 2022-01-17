package id.lemonavy.dalenta.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.db.entity.ItemData

@Dao
interface ProductDao {

    @Query("SELECT * FROM tb_items")
    fun getItems(): LiveData<MutableList<ItemData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(data : ItemData)

    @Query("DELETE FROM tb_items WHERE id = :id")
    fun deleteDataById(id: Long?)

    @Query("SELECT * FROM tb_cart")
    fun getCart(): LiveData<MutableList<CartData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(data : CartData)

    @Query("SELECT * FROM tb_cart WHERE id = :id")
    fun getItemByUnique(id: Long): CartData

    @Query("UPDATE tb_cart SET item_size = :size, item_quantity = :quantity, item_price = :price, item_price_topping = :topping , item_note = :notes, tax_service = :service, tax_gst = :gst WHERE id = :id")
    fun updateCartById( id: String?, size: String, quantity: String, price: Int, topping: Int,
                        notes: String, service: Boolean, gst: Boolean)

    @Update
    fun updateCartById(data : CartData)

    @Query("SELECT COUNT(" + "id" + ") as total " + " FROM " + "tb_items")
    fun countItem(): Int
}