package com.demo.habitcheck.ui.editnote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import com.demo.habitcheck.utils.Constants.DayInWeek.FRIDAY
import com.demo.habitcheck.utils.Constants.DayInWeek.MONDAY
import com.demo.habitcheck.utils.Constants.DayInWeek.SATURDAY
import com.demo.habitcheck.utils.Constants.DayInWeek.SUNDAY
import com.demo.habitcheck.utils.Constants.DayInWeek.THURSDAY
import com.demo.habitcheck.utils.Constants.DayInWeek.TUESDAY
import com.demo.habitcheck.utils.Constants.DayInWeek.WEDNESDAY
import com.demo.habitcheck.utils.DateUtils
import com.demo.habitcheck.utils.RemindType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditTaskViewModel @Inject constructor(val taskRepository: TaskRepository) : ViewModel() {
    val task = MutableLiveData<Task>()
    val progressText = MutableLiveData<String>()
    val progress = MutableLiveData<Int>()
    val isMonSelected = MutableLiveData(false)
    val isTueSelected = MutableLiveData(false)
    val isWendSelected = MutableLiveData(false)
    val isThursSelected = MutableLiveData(false)
    val isFriSelected = MutableLiveData(false)
    val isSatSelected = MutableLiveData(false)
    val isSunSelected = MutableLiveData(false)
    val isSaveTaskResult = MutableLiveData<Boolean>()
    val isEveryday = MutableLiveData(false)
    val isOneTime = MutableLiveData(false)
    val isSomeDay = MutableLiveData(false)
    val isSave = MutableLiveData(false)
    val isEditTime = MutableLiveData(false)
    var obsFrequency: RemindType = RemindType.ONE_TIME
    var remindInMillis = 0L

    fun setUpData() {
        task.value?.progress?.let {
            progressText.value = "$it/100"
            progress.value = it
        }

        when (task.value?.frequency) {
            RemindType.ONE_TIME -> isOneTime.value = true
            RemindType.EVERY_DAY -> isEveryday.value = true
            RemindType.SOME_DAY -> isSomeDay.value = true
        }

        task.value?.remindDate?.split(" ")?.let {
            isMonSelected.value = it.contains(MONDAY)
            isTueSelected.value = it.contains(TUESDAY)
            isWendSelected.value = it.contains(WEDNESDAY)
            isThursSelected.value = it.contains(THURSDAY)
            isFriSelected.value = it.contains(FRIDAY)
            isSatSelected.value = it.contains(SATURDAY)
            isSunSelected.value = it.contains(SUNDAY)
        }
    }

    fun onSave() {
        isSave.value = true
    }

    fun saveTask(title: String?, des: String?) {
        if (title.isNullOrEmpty() || des.isNullOrEmpty()) {
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
            taskRepository.updateTask(
                Task(
                    title = title,
                    description = des,
                    progress = progress.value,
                    remindDate = getRemindDay(),
                    frequency = obsFrequency,
                    remindInMillis = this@EditTaskViewModel.remindInMillis,
                    remindTime = DateUtils.convertDateToDay(remindInMillis.toString()),
                    id = task.value?.id,
                    workerId = task.value?.workerId
                )
            )
            isSaveTaskResult.value = true
        }
    }

    private fun getRemindDay(): String {
        var remindDay = ""
        if (isMonSelected.value == true) {
            remindDay += MONDAY
        }
        if (isTueSelected.value == true) {
            remindDay += " $TUESDAY"
        }
        if (isWendSelected.value == true) {
            remindDay += " $WEDNESDAY"
        }
        if (isThursSelected.value == true) {
            remindDay += " $THURSDAY"
        }
        if (isFriSelected.value == true) {
            remindDay += " $FRIDAY"
        }
        if (isSatSelected.value == true) {
            remindDay += " $SATURDAY"
        }
        if (isSunSelected.value == true) {
            remindDay += " $SUNDAY"
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