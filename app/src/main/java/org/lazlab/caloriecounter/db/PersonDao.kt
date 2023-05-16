package org.lazlab.caloriecounter.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PersonDao {
    @Insert
    fun insert(person: PersonEntity)

    @Query("SELECT * FROM person ORDER BY id DESC")
    fun getLastPerson(): LiveData<List<PersonEntity>>

    @Query("DELETE FROM person")
    fun clearData()
}