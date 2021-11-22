package com.demo.habitcheck.ui.addnote

import android.util.Log
import androidx.lifecycle.ViewModel
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
    fun addTask() {
        CoroutineScope(Dispatchers.Main).launch {
            taskRepository.insertTask(Task(title = "Test"))
            val tasks = taskRepository.getAllTask()
            Log.d("TAG1", tasks.size.toString())
        }
    }
}
