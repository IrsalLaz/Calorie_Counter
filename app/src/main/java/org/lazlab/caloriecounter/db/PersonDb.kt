package org.lazlab.caloriecounter.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PersonEntity::class], version = 1, exportSchema = false)
abstract class PersonDb : RoomDatabase() {
    abstract val dao: PersonDao

    companion object {
        @Volatile
        private var INSTANCE: PersonDb? = null

        fun getInstance(context: Context): PersonDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PersonDb::class.java,
                        "bmi.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}