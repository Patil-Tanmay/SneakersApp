package com.tanmay.sneakerapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanmay.sneakerapp.data.Converters
import com.tanmay.sneakerapp.data.SneakerItem

@Database(entities = [SneakerItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class CartDatabase : RoomDatabase(){
    abstract fun getCartDao(): CartDao
}