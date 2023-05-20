package org.lazlab.caloriecounter.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lazlab.caloriecounter.db.PersonDao

class HistoryViewModel(private val db: PersonDao) : ViewModel() {
    val data = db.getLastPerson()

    fun deleteData() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.clearData()
        }
    }
}