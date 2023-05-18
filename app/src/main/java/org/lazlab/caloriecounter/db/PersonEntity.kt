package org.lazlab.caloriecounter.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var date: Long = System.currentTimeMillis(),
    var weight: Float,
    var height: Float,
    var age: Float,
    var isMale: Boolean,
    var dailyActivity: Float
)
