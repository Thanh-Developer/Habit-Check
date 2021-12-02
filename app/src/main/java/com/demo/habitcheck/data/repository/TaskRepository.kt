package com.demo.habitcheck.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.demo.habitcheck.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun getAllTask(): List<Task>
    fun getAllNotePaged(config: PagingConfig): Flow<PagingData<Task>>
    fun getAllNoteNotDonePaged(config: PagingConfig): Flow<PagingData<Task>>
    suspend fun getTaskById(id: Int): Task
}