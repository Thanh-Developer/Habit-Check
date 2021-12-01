package com.demo.habitcheck.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.demo.habitcheck.data.local.TaskDAO
import com.demo.habitcheck.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImp @Inject constructor(
    private val taskDAO: TaskDAO
) : TaskRepository {
    override suspend fun insertTask(task: Task) = taskDAO.insertTask(task)

    override suspend fun deleteTask(task: Task) = taskDAO.deleteTask(task)

    override suspend fun updateTask(task: Task) = taskDAO.updateTask(task)

    override suspend fun getAllTask() = taskDAO.getAllTask()

    override fun getAllNotePaged(config: PagingConfig): Flow<PagingData<Task>> {
        return Pager(config = config) { taskDAO.getAllNotePaged() }.flow
    }

    override suspend fun getTaskById(id: Int) = taskDAO.getTaskById(id)
}