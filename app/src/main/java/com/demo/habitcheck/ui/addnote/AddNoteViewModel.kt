package com.demo.habitcheck.ui.addnote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import com.demo.habitcheck.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(private val taskRepository: TaskRepository) :
    ViewModel() {

    val isMonSelected = MutableLiveData(false)
    val isTueSelected = MutableLiveData(false)
    val isWendSelected = MutableLiveData(false)
    val isThursSelected = MutableLiveData(false)
    val isFriSelected = MutableLiveData(false)
    val isSatSelected = MutableLiveData(false)
    val isSunSelected = MutableLiveData(false)
    val remindTime = MutableLiveData("00:00")
    val isSaveTaskResult = MutableLiveData<Boolean>()

    fun saveTask(title: String?, des: String?) {
        if (title.isNullOrEmpty() || des.isNullOrEmpty()) {
            isSaveTaskResult.value = false
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            taskRepository.insertTask(
                Task(
                    title = title,
                    description = des,
                    progress = 0,
                    remindDate = getRemindDay(),
                    remindTime = remindTime.value
                )
            )
            isSaveTaskResult.value = true
        }
    }

    private fun getRemindDay(): String {
        var remindDay = ""
        if (isMonSelected.value == true) {
            remindDay += Constants.DayInWeek.MONDAY
        }
        if (isTueSelected.value == true) {
            remindDay += " ${Constants.DayInWeek.TUESDAY}"
        }
        if (isWendSelected.value == true) {
            remindDay += " ${Constants.DayInWeek.WEDNESDAY}"
        }
        if (isThursSelected.value == true) {
            remindDay += " ${Constants.DayInWeek.THURSDAY}"
        }
        if (isFriSelected.value == true) {
            remindDay += " ${Constants.DayInWeek.FRIDAY}"
        }
        if (isSatSelected.value == true) {
            remindDay += " ${Constants.DayInWeek.SATURDAY}"
        }
        if (isSunSelected.value == true) {
            remindDay += " ${Constants.DayInWeek.SUNDAY}"
        }
        return remindDay.trim()
    }

    fun onMondaySelected() {
        isMonSelected.value = isMonSelected.value?.let {
            !it
        } ?: run { false }
    }

    fun onTuesdaySelected() {
        isTueSelected.value = isTueSelected.value?.let {
            !it
        } ?: run { false }
    }

    fun onWednesdaySelected() {
        isWendSelected.value = isWendSelected.value?.let {
            !it
        } ?: run { false }
    }

    fun onThursSelected() {
        isThursSelected.value = isThursSelected.value?.let {
            !it
        } ?: run { false }
    }

    fun onFridaySelected() {
        isFriSelected.value = isFriSelected.value?.let {
            !it
        } ?: run { false }
    }

    fun onSaturdaySelected() {
        isSatSelected.value = isSatSelected.value?.let {
            !it
        } ?: run { false }
    }

    fun onSundaySelected() {
        isSunSelected.value = isSunSelected.value?.let {
            !it
        } ?: run { false }
    }
}
