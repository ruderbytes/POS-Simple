package id.lemonavy.dalenta.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.lemonavy.dalenta.db.dao.ProductDao
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.db.entity.ItemData


@Database(entities = [ItemData::class, CartData::class], version = 1)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun itemDao(): ProductDao

    companion object {
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper? {
            if (INSTANCE == null) {
                synchronized(DatabaseHelper::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DatabaseHelper::class.java, "dalenta_sales.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}