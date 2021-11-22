package com.demo.habitcheck.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.demo.habitcheck.data.local.TaskDAO
import com.demo.habitcheck.data.model.Task
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
    override fun getAllNotePaged(config: PagedList.Config): LiveData<PagedList<Task>> {
        val factory: DataSource.Factory<Int, Task> = taskDAO.getAllNotePaged()
        return LivePagedListBuilder(factory, config).build()
    }
}