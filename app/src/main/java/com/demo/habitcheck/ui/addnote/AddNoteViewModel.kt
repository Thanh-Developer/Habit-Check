package com.demo.habitcheck.ui.addnote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import com.demo.habitcheck.utils.Constants
import com.demo.habitcheck.utils.DateUtils.convertDateToDay
import com.demo.habitcheck.utils.RemindType
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
    val isSaveTaskResult = MutableLiveData<Boolean>()
    val isEveryday = MutableLiveData(false)
    val isOneTime = MutableLiveData(true)
    val isSomeDay = MutableLiveData(false)
    val isClose = MutableLiveData(false)
    val isDone = MutableLiveData(false)
    var title = MutableLiveData<String>().apply { value = "" }
    var des = MutableLiveData<String>().apply { value = "" }
    var obsFrequency: RemindType = RemindType.ONE_TIME

    var remindInMillis = 0L

    fun onDone() {
        isDone.value = true
    }

    fun saveTask() {
        if (title.value.isNullOrEmpty() || des.value.isNullOrEmpty()) {
            isSaveTaskResult.value = false
            return
        }
        if (isOneTime.value == true) {
            obsFrequency = RemindType.ONE_TIME
        }

        if (isEveryday.value == true) {
            obsFrequency = RemindType.EVERY_DAY
        }

        if (isSomeDay.value == true) {
            obsFrequency = RemindType.SOME_DAY
        }

        CoroutineScope(Dispatchers.Main).launch {
            taskRepository.insertTask(
                Task(
                    title = title.value,
                    description = des.value,
                    progress = 0,
                    frequency = obsFrequency,
                    remindDate = getRemindDay(),
                    remindInMillis = this@AddNoteViewModel.remindInMillis,
                    remindTime = convertDateToDay(remindInMillis.toString())
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

    fun onClose() {
        isClose.value = true
    }
}
