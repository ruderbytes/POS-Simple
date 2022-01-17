package id.lemonavy.dalenta.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tb_cart")
@Parcelize
data class CartData(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "item_id")var itemId: String,
        @ColumnInfo(name = "item_name")var itemName: String,
        @ColumnInfo(name = "item_size")var itemSize: String,
        @ColumnInfo(name = "item_quantity")var itemQty: String,
        @ColumnInfo(name = "item_price")var itemPrice: Int,
        @ColumnInfo(name = "item_price_topping")var itemPriceTopping: Int,
        @ColumnInfo(name = "item_note")var itemNote: String?=null,
        @ColumnInfo(name = "tax_service")var taxService: Boolean,
        @ColumnInfo(name = "tax_gst")var taxGst: Boolean,
): Parcelable