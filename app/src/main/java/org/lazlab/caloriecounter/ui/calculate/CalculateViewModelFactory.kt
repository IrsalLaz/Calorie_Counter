package org.lazlab.caloriecounter.ui.calculate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lazlab.caloriecounter.db.PersonDao

class CalculateViewModelFactory(
    private val db: PersonDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CalculateViewModel::class.java)) {
            return CalculateViewModel(db) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }

}