package id.lemonavy.dalenta.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.db.entity.ItemData
import id.lemonavy.dalenta.repository.ItemRepository
import id.lemonavy.dalenta.util.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ItemRepository

    init {
        val userDao = DatabaseHelper.getInstance(application)!!.itemDao()
        repository = ItemRepository(userDao)
    }

    private var _product: LiveData<MutableList<ItemData>> =
        MutableLiveData()

    fun productState() = _product

    fun getItems() {
        _product = repository.getItems()
    }

    fun addToCart(data: CartData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCart(data)
        }
    }
}