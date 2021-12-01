package com.demo.habitcheck.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.PagingConfig
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import com.demo.habitcheck.utils.Constants.PAGE_INITIAL_LOAD_SIZE_HINT
import com.demo.habitcheck.utils.Constants.PAGE_PREFETCH_DISTANCE
import com.demo.habitcheck.utils.Constants.PAGE_SIZE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(val taskRepository: TaskRepository) : ViewModel() {
    private val config = PagingConfig(
        // Number of items loaded for a page in one go from DataSource
        pageSize = PAGE_SIZE,
        // The number of items in the first load, if not set, it defaults = 3 * pageSize
        initialLoadSize = PAGE_INITIAL_LOAD_SIZE_HINT,
        // Determine the distance (number of items) from the loaded content to load the data, if not set, it defaults to pageSize.
        prefetchDistance = PAGE_PREFETCH_DISTANCE,
        // PagedList will display null placeholders for items that have not been loaded content, by default it will be true.
        enablePlaceholders = true
    )

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    fun getAllNotePaged() = taskRepository.getAllNotePaged(config)

    fun deleteTask(task: Task) {
        CoroutineScope(Dispatchers.Main).launch {
            taskRepository.deleteTask(task)
        }
    }
}