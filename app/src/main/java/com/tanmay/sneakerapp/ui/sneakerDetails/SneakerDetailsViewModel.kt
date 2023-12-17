package com.tanmay.sneakerapp.ui.sneakerDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.sneakerapp.data.SneakerItem
import com.tanmay.sneakerapp.data.repo.SneakerRepository
import com.tanmay.sneakerapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SneakerDetailsViewModel @Inject constructor(
    private val sneakerRepository: SneakerRepository
) : ViewModel() {

    private val _addOrRemoveSneakerToCart = Channel<Resource<SneakerItem>>(1)
    val addOrRemoveSneakerToCart = _addOrRemoveSneakerToCart.receiveAsFlow()

    fun addOrRemoveSneakerFromCart(sneakerItem: SneakerItem, isAddToCart: Boolean) {
        viewModelScope.launch {
            try {
                _addOrRemoveSneakerToCart.send(Resource.Loading)
                if (isAddToCart) {
                    sneakerRepository.addSneakerToCart(sneakerItem)
                } else {
                    sneakerRepository.removeSneakerFromCart(sneakerItem)
                }
                _addOrRemoveSneakerToCart.send(Resource.Success(sneakerItem.copy(isAddedToCart = isAddToCart)))

            } catch (e: Exception) {
                _addOrRemoveSneakerToCart.send(Resource.Error("Unable to Add to cart! \n ${e.localizedMessage}"))
            }
        }
    }

//    fun doesItemExistInCart(sneakerItem: SneakerItem) {
//        viewModelScope.launch {
//            try {
//                _addOrRemoveSneakerToCart.send(Resource.Loading)
//                val addToCart
//                _addOrRemoveSneakerToCart.send(Resource.Success(sneakerItem.copy(isAddedToCart = isAddToCart)))
//
//            } catch (e: Exception) {
//                _addOrRemoveSneakerToCart.send(Resource.Error("Unable to Add to cart! \n ${e.localizedMessage}"))
//            }
//        }
//    }


}