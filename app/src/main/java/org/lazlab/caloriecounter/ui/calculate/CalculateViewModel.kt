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
import org.lazlab.caloriecounter.model.BmiScore
import org.lazlab.caloriecounter.model.BmrScore
import org.lazlab.caloriecounter.model.calculateBmi
import org.lazlab.caloriecounter.model.calculateBmr

class CalculateViewModel(private val db: PersonDao) : ViewModel() {
    private val scoreBmi = MutableLiveData<BmiScore?>()
    private val scoreBmr = MutableLiveData<BmrScore?>()

    fun calculate(weight: Float, height: Float, age: Float, isMale: Boolean, dailyActivity: Int) {
        val dataBmi = PersonEntity(
            weight = weight,
            height = height,
            age = age,
            isMale = isMale,
            dailyActivity = dailyActivity
        )

        //calculate BMR & BMI score value
        scoreBmi.value = dataBmi.calculateBmi()
        scoreBmr.value = dataBmi.calculateBmr()

        //insert data to DB
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.insert(dataBmi)
            }
        }
    }

    fun getBmiScore(): LiveData<BmiScore?>   = scoreBmi
    fun getBmrScore(): LiveData<BmrScore?> = scoreBmr

}