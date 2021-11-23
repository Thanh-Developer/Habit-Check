package com.demo.habitcheck.di

import android.app.Application
import androidx.room.Room
import com.demo.habitcheck.data.local.AppDatabase
import com.demo.habitcheck.data.local.TaskDAO
import com.demo.habitcheck.data.repository.TaskRepository
import com.demo.habitcheck.data.repository.TaskRepositoryImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "database.db").build()

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDAO = database.taskDao()

    @Singleton
    @Provides
    fun provideTaskRepository(taskDAO: TaskDAO): TaskRepository = TaskRepositoryImp(taskDAO)
}