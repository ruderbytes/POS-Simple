package id.lemonavy.dalenta.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.repository.ItemRepository
import id.lemonavy.dalenta.util.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ItemRepository

    init {
        val userDao = DatabaseHelper.getInstance(application)!!.itemDao()
        repository = ItemRepository(userDao)
    }

    private var _cart: LiveData<MutableList<CartData>> =
        MutableLiveData()

    fun cartState() = _cart

    fun getCart() {
        _cart = repository.getCart()
    }

    fun getUniqueItem(id: Long): CartData {
        return repository.getItemId(id)
    }

    fun updateCartId(data: CartData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCartById(
                data.id.toString(), data.itemSize,
                data.itemQty,  data.itemPrice,
                data.itemPriceTopping, data.itemNote.toString(),
                data.taxService, data.taxGst)
        }
    }

    fun addToCart(data: CartData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCart(data)
        }
    }
}