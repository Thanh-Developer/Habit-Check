package com.demo.habitcheck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.habitcheck.data.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO
}