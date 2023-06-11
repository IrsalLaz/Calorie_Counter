package org.lazlab.caloriecounter.ui.calculate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lazlab.caloriecounter.db.PersonDao
import org.lazlab.caloriecounter.db.PersonEntity
import org.lazlab.caloriecounter.model.Category
import org.lazlab.caloriecounter.model.Results
import org.lazlab.caloriecounter.model.calculateBmiBmr
import org.lazlab.caloriecounter.network.UpdateWorker
import java.util.concurrent.TimeUnit

class CalculateViewModel(private val db: PersonDao) : ViewModel() {
    private val scoreBmiBmr = MutableLiveData<Results?>()
    private val navigate = MutableLiveData<Category?>()


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
        navigate.value = scoreBmiBmr.value?.category
    }

    fun endNavigate() {
        navigate.value = null
    }

    fun getNavigate(): MutableLiveData<Category?> = navigate

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(3, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}