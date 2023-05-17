package org.lazlab.caloriecounter.model

import org.lazlab.caloriecounter.db.PersonEntity

//calculate bmi and bmr score
fun PersonEntity.calculateBmi(): BmiScore {
    val heightCm = height / 100
    val bmi = weight / (heightCm * heightCm)
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
    return BmiScore(bmi, category)
}

fun PersonEntity.calculateBmr(): BmrScore{
    val bmr: Float = if (isMale) {
        ((88.4f + 13.4f * weight) + (4.8f * height) - (5.68f * age)) * dailyActivity
    } else {
        ((447.6f + 9.25f * weight) + (3.10f * height) - (4.33f * age)) * dailyActivity
    }
    return BmrScore(bmr)
}