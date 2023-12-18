package com.tanmay.sneakerapp.data.repo

import android.content.Context
import androidx.room.withTransaction
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tanmay.sneakerapp.data.SneakerItem
import com.tanmay.sneakerapp.db.CartDatabase
import com.tanmay.sneakerapp.utils.parseList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class SneakerRepository @Inject constructor(
    private val cartDb: CartDatabase,
    private val applicationContext: Context
) {

    private val cartDao by lazy { cartDb.getCartDao() }

    suspend fun getSneakerList(): List<SneakerItem> =
        withContext(Dispatchers.IO) {
            if (cartDao.getRowCount() == 0) {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val lOfSneakers = moshi.parseList<SneakerItem>(getResponseData())
                cartDb.withTransaction {
                    cartDao.insertSneakersList(lOfSneakers ?: emptyList())
                    cartDao.getListOfSneakers()
                }

            } else {
                cartDao.getListOfSneakers()
            }
        }

    suspend fun getSearchedSneakerList(searchString: String): List<SneakerItem> = withContext(Dispatchers.IO) {
            cartDao.getSearchedSneakerList(searchString)
        }


    private fun getResponseData(): String {
        val inputStream = applicationContext.resources.assets.open("sneakers.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        var line: String? = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }

        bufferedReader.close()
        return stringBuilder.toString()
    }

    suspend fun getCartAddedSneakers(): List<SneakerItem> = withContext(Dispatchers.IO) {
        cartDao.getCartAddedSneakers()
    }

    suspend fun removeSneakerFromCart(sneakerItem: SneakerItem) = withContext(Dispatchers.IO) {
        cartDao.deleteSneakerFromCart(sneakerItem.id)
    }

    suspend fun addSneakerToCart(sneakerItem: SneakerItem) = withContext(Dispatchers.IO) {
        cartDao.updateSneakerInfo(sneakerItem.id, isAddedToCart = true)
    }

    suspend fun deleteCartTable() = withContext(Dispatchers.IO) {
        cartDao.deleteCartTable()
    }

}