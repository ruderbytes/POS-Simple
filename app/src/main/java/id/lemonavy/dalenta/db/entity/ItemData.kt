package id.lemonavy.dalenta.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tb_items")
@Parcelize
data class ItemData(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "item_image")var itemImage: String,
        @ColumnInfo(name = "item_name")var itemName: String,
        @ColumnInfo(name = "item_price")var itemPrice: Int,
        @ColumnInfo(name = "item_additional")var itemAdditional: Int
): Parcelable