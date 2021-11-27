package com.demo.habitcheck

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
    val isGetTaskSuccess = MutableLiveData(false)
    lateinit var task: Task

    fun getTask(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            task = taskRepository.getTaskById(id)
            isGetTaskSuccess.value = true
        }
    }
}