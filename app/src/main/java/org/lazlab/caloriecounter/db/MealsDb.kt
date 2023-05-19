package org.lazlab.caloriecounter.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MealsEntity::class], version = 1, exportSchema = false)

abstract class MealsDb : RoomDatabase() {
    abstract val dao: MealsDao

    companion object {
        @Volatile
        private var INSTANCE: MealsDb? = null

        fun getInstance(context: Context): MealsDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MealsDb::class.java,
                        "meals.db"
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