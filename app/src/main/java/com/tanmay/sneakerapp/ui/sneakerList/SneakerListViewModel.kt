package com.tanmay.sneakerapp.ui.sneakerList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.sneakerapp.data.SneakerItem
import com.tanmay.sneakerapp.data.repo.SneakerRepository
import com.tanmay.sneakerapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SneakerListViewModel @Inject constructor(
    private val sneakerRepository: SneakerRepository
) : ViewModel(){

    private val _sneakerList = Channel<Resource<List<SneakerItem>>>(1)
    val sneakerList = _sneakerList.receiveAsFlow()

    init {
//        getSneakersList(null)
    }

    fun getSneakersList(query: String?){
        viewModelScope.launch {
            try {
                _sneakerList.send(Resource.Loading)
                val lOfSneakers = when(true){
                    (query != null) -> {
                        sneakerRepository.getSearchedSneakerList(query)
                    }

                    else -> {
                        Log.d("TAGG", "getSneakersList: entering if")
                        sneakerRepository.getSneakerList()
                    }
                }
                if (!lOfSneakers.isNullOrEmpty()){
                    _sneakerList.send(Resource.Success(lOfSneakers))
                }else{
                    _sneakerList.send(Resource.Error("No Sneakers Found !"))
                }
            }catch (e:Exception){
                Log.e("TAGG", "getSneakersList: ${e.printStackTrace()}", )
                _sneakerList.send(Resource.Error("Unable to fetch Sneakers! \n ${e.localizedMessage}"))
            }
        }
    }



}