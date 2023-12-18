package com.tanmay.sneakerapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.sneakerapp.data.repo.SneakerRepository
import com.tanmay.sneakerapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SneakerRepository
):ViewModel() {

    fun deleteCartTable(){
        viewModelScope.launch {
            try {
                repository.deleteCartTable()
            }catch (e:Exception){
                Log.e("MainViewModel", "${e.printStackTrace()} ", )
            }
        }
    }


}