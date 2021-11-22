package com.demo.habitcheck.data.repository

import com.demo.habitcheck.data.model.Task

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun getAllTask(): List<Task>
}