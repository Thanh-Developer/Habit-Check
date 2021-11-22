package com.demo.habitcheck.ui.editnote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import javax.inject.Inject

class EditTaskViewModel @Inject constructor(val taskRepository: TaskRepository) : ViewModel() {
    val task = MutableLiveData<Task>()
}