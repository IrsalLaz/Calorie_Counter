package org.lazlab.caloriecounter.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var date: Long = System.currentTimeMillis(),
    var mealName: String,
    var calorie: Double
)
