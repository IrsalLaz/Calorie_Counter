package org.lazlab.caloriecounter.ui.calculate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lazlab.caloriecounter.db.PersonDao
import org.lazlab.caloriecounter.db.PersonEntity
import org.lazlab.caloriecounter.model.Category
import org.lazlab.caloriecounter.model.Results
import org.lazlab.caloriecounter.model.calculateBmiBmr

class CalculateViewModel(private val db: PersonDao) : ViewModel() {
    private val scoreBmiBmr = MutableLiveData<Results?>()
    private val navigate = MutableLiveData<Float?>()

//    private val navigate = MutableLiveData<Results?>()

    fun calculate(weight: Float, height: Float, age: Float, isMale: Boolean, dailyActivity: Float) {
        val dataPerson = PersonEntity(
            weight = weight,
            height = height,
            age = age,
            isMale = isMale,
            dailyActivity = dailyActivity
        )

        //calculate BMR & BMI score value
        scoreBmiBmr.value = dataPerson.calculateBmiBmr()

        //insert data to DB
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.insert(dataPerson)
            }
        }
    }

    fun getBmiBmrScore(): LiveData<Results?> = scoreBmiBmr

    fun startNavigate() {
        navigate.value = scoreBmiBmr.value?.bmr
    }

    fun endNavigate() {
        navigate.value = null
    }

    fun getNavigate(): MutableLiveData<Float?> = navigate
}