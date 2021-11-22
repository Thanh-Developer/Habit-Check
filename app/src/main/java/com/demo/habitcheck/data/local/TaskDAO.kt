package com.demo.habitcheck.data.local

import androidx.room.*
import com.demo.habitcheck.data.model.Task
import dagger.Module
import dagger.Provides

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task")
    suspend fun getAllTask(): List<Task>

    @Update
    suspend fun updateTask(task: Task)
}