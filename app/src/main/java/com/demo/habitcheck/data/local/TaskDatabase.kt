package com.demo.habitcheck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.habitcheck.data.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO
}