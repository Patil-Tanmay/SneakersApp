package com.tanmay.sneakerapp.ui.sneakerList

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

    private val _sneakerList = MutableStateFlow<Resource<List<SneakerItem>>>(Resource.Loading)
    val sneakerList = _sneakerList.asStateFlow()

    fun getSneakersList(query: String?){
        viewModelScope.launch {
            try {
                _sneakerList.emit(Resource.Loading)
                val lOfSneakers = when(true){
                    (query != null) -> {
                        sneakerRepository.getSearchedSneakerList(query)
                    }

                    else -> {
                        sneakerRepository.getSneakerList()
                    }
                }
                if (!lOfSneakers.isNullOrEmpty()){
                    _sneakerList.emit(Resource.Success(lOfSneakers))
                }else{
                    _sneakerList.emit(Resource.Error("No Sneakers Found !"))
                }
            }catch (e:Exception){
                _sneakerList.emit(Resource.Error("Unable to fetch Sneakers! \n ${e.localizedMessage}"))
            }
        }
    }



}