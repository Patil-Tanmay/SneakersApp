package com.tanmay.sneakerapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tanmay.sneakerapp.data.SneakerItem

@Dao
interface CartDao {

    @Query("SELECT COUNT(*) FROM cart")
    suspend fun getRowCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSneakersList(sneakers: List<SneakerItem>) : List<Long>

    @Query("SELECT * FROM cart")
    suspend fun getListOfSneakers(): List<SneakerItem>

    @Query("DELETE FROM cart")
    suspend fun deleteCartTable()

    // Find a list of elements where columnName contains a specific part of the given string
    @Query("SELECT * FROM cart WHERE name LIKE :searchString")
    suspend fun getSearchedSneakerList(searchString: String): List<SneakerItem>


    @Query("SELECT * FROM cart WHERE isAddedToCart = 1")
    suspend fun getCartAddedSneakers(): List<SneakerItem>

    @Query("UPDATE cart SET isAddedToCart = :isAddedToCart WHERE id = :id")
    suspend fun updateSneakerInfo(id: String, isAddedToCart: Boolean)

    @Transaction
    suspend fun deleteSneakerFromCart(id: String): List<SneakerItem>{
        updateSneakerInfo(id,false)
        return getCartAddedSneakers()
    }

    @Query("SELECT isAddedToCart FROM Cart WHERE id = :sneakerId")
    fun doesSneakerExistInCart(sneakerId: String): Boolean


}