package com.tanmay.sneakerapp.ui.sneakerCart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.sneakerapp.data.SneakerItem
import com.tanmay.sneakerapp.data.repo.SneakerRepository
import com.tanmay.sneakerapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SneakerCartViewModel @Inject constructor(
    private val sneakerRepository: SneakerRepository
) : ViewModel() {

    private val _cartSneakerList =
        MutableStateFlow<Resource<List<SneakerItem>>>(Resource.Loading)
    val cartSneakerList = _cartSneakerList.asStateFlow()

    fun getSneakersFromCart() {
        viewModelScope.launch {
            try {
                _cartSneakerList.emit(Resource.Loading)
                val lOfCartSneakers = sneakerRepository.getCartAddedSneakers()
                _cartSneakerList.emit(Resource.Success(lOfCartSneakers))
            } catch (e: Exception) {
                _cartSneakerList.emit(Resource.Error("Unable to fetch Sneakers! \n ${e.localizedMessage}"))
            }
        }
    }

    fun removeSneakerFromCart(sneakerItem: SneakerItem) {
        viewModelScope.launch {
            try {
                _cartSneakerList.emit(Resource.Loading)
                val lOfCartSneakers = sneakerRepository.removeSneakerFromCart(sneakerItem)
                _cartSneakerList.emit(Resource.Success(lOfCartSneakers))
            } catch (e: Exception) {
                _cartSneakerList.emit(Resource.Error("Unable to remove Sneaker From Cart! \n ${e.localizedMessage}"))
            }
        }
    }


}