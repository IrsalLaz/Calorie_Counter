package org.lazlab.caloriecounter.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MealsDao {
    @Insert
    fun insert(meal: MealsEntity)

    @Query("SELECT * FROM meals ORDER BY id DESC")
    fun getLastMeal(): LiveData<List<MealsEntity>>

    @Query("DELETE FROM meals")
    fun clearData()

}