package com.demo.habitcheck.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.demo.habitcheck.data.model.Task

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

    @Query("SELECT * FROM task")
    fun getAllNotePaged(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id: Int): Task
}