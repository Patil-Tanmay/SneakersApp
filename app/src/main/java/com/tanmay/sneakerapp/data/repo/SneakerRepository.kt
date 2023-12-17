package com.tanmay.sneakerapp.data.repo

import android.content.Context
import android.util.Log
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
import kotlin.math.log

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

                Log.d("TAGG", "getSneakerList: $lOfSneakers")
                /*val response = cartDao.insertSneakersList(lOfSneakers?: emptyList())
                if (!response.isNullOrEmpty()) {
                    Log.d("TAGG", "getSneakerList: enting if")
                    cartDao.getListOfSneakers()
                }else{
                    emptyList()
                }*/

                cartDb.withTransaction {
                    cartDao.insertSneakersList(lOfSneakers?: emptyList())
                    cartDao.getListOfSneakers()
                }

            } else {
                Log.d("TAGG", "getSneakerList: entering else")
                cartDao.getListOfSneakers()
            }
//            lOfSneakers ?: emptyList()
        }

    suspend fun getSearchedSneakerList(searchString: String): List<SneakerItem> =
        withContext(Dispatchers.IO) { cartDao.getSearchedSneakerList(searchString) }


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
        Log.d("TAGG", "getResponseData: ${stringBuilder.toString()}")
        return stringBuilder.toString()
    }

    suspend fun getCartAddedSneakers(): List<SneakerItem> = withContext(Dispatchers.IO) {
        val cartDao = cartDb.getCartDao()

        cartDao.getCartAddedSneakers()
    }

    suspend fun removeSneakerFromCart(sneakerItem: SneakerItem) = withContext(Dispatchers.IO) {
//        cartDb.withTransaction {
//            cartDao.deleteSneaker(sneakerItem.id)
//            cartDao.getCartAddedSneakers()
//        }
        cartDao.deleteSneakerFromCart(sneakerItem.id)
    }

    suspend fun addSneakerToCart(sneakerItem: SneakerItem) = withContext(Dispatchers.IO) {
        cartDao.updateSneakerInfo(sneakerItem.id, isAddedToCart = true)
    }


}