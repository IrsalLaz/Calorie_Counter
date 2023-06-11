package org.lazlab.caloriecounter.ui.results

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.lazlab.caloriecounter.model.Meals
import org.lazlab.caloriecounter.network.ApiStatus
import org.lazlab.caloriecounter.network.MealsApi

class ResultViewModel : ViewModel() {

    private val data = MutableLiveData<List<Meals>>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                data.postValue((MealsApi.service.getMeals()))
                status.postValue(ApiStatus.SUCCESS)
                Log.d("ResultViewModel", "Success")
            } catch (e: Exception) {
                Log.d("ResultViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun getData(): LiveData<List<Meals>> = data
    fun getStatus(): LiveData<ApiStatus> = status
}