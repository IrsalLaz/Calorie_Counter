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

    fun calculateBmiBmr(
        weight: Float,
        height: Float,
        age: Float,
        isMale: Boolean,
        dailyActivity: Float
    ) {
        //calculate BMI
        val heightCm = height / 100
        val bmi = weight / (heightCm * heightCm)
        val category = getCategory(bmi, isMale)

        //calculate BMR
        val bmr: Float = if (isMale) {
            ((88.4f + 13.4f * weight) + (4.8f * height) - (5.68f * age)) * dailyActivity
        } else {
            ((447.6f + 9.25f * weight) + (3.10f * height) - (4.33f * age)) * dailyActivity
        }

        scoreBmiBmr.value = Results(bmi, bmr, category)
    }

    private fun getCategory(bmi: Float, isMale: Boolean): Category {
        val category = if (isMale) {
            when {
                bmi < 20.5 -> Category.KURUS
                bmi >= 27.0 -> Category.GEMUK
                else -> Category.IDEAL
            }
        } else {
            when {
                bmi < 18.5 -> Category.KURUS
                bmi >= 25.0 -> Category.GEMUK
                else -> Category.IDEAL
            }
        }
        return category
    }

    fun getBmiBmrScore(): LiveData<Results?> = scoreBmiBmr

}