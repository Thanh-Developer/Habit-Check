package com.demo.habitcheck.ui.addnote

import android.os.Bundle
import android.util.Log
import androidx.work.*
import com.demo.habitcheck.databinding.ActivityAddNoteBinding
import com.demo.habitcheck.service.NotifyWorker
import com.demo.habitcheck.service.NotifyWorker.Companion.NOTIFICATION_ID
import com.demo.habitcheck.service.NotifyWorker.Companion.NOTIFICATION_WORK
import com.demo.habitcheck.utils.UtilExtensions.myToast
import com.demo.habitcheck.utils.UtilExtensions.updateDayUI
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddNoteActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: AddNoteViewModel
    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observeField()
    }

    private fun initView() {
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            viewModel = this@AddNoteActivity.viewModel
            lifecycleOwner = this@AddNoteActivity
            executePendingBindings()
        }
    }

    private fun observeField() {
        viewModel.apply {
            binding.apply {
                isMonSelected.observe(this@AddNoteActivity, {
                    updateDayUI(applicationContext, listDay.txtMonday, it ?: false)
                })
                isTueSelected.observe(this@AddNoteActivity, {
                    updateDayUI(applicationContext, listDay.txtTuesday, it ?: false)
                })
                isWendSelected.observe(this@AddNoteActivity, {
                    updateDayUI(applicationContext, listDay.txtWednesday, it ?: false)
                })
                isThursSelected.observe(this@AddNoteActivity, {
                    updateDayUI(applicationContext, listDay.txtThursday, it ?: false)
                })
                isFriSelected.observe(this@AddNoteActivity, {
                    updateDayUI(applicationContext, listDay.txtFriday, it ?: false)
                })
                isSatSelected.observe(this@AddNoteActivity, {
                    updateDayUI(applicationContext, listDay.txtSaturday, it ?: false)
                })
                isSunSelected.observe(this@AddNoteActivity, {
                    updateDayUI(applicationContext, listDay.txtSunday, it ?: false)
                })
            }

            isDone.observe(this@AddNoteActivity, {
                if (it == true) {
                    if (isOneTime.value == true) {
                        setupRemindOneTime()
                    }
                    if (isEveryday.value == true) {
                        setupRemindEveryDay()
                    }
                    if (isSomeDay.value == true) {
                        setupRemindSomeDay()
                    }
                    saveTask()
                }
            })

            isSaveTaskResult.observe(this@AddNoteActivity, {
                if (it == true) {
                    myToast("Save task success")
                    finish()
                } else {
                    myToast("Title and des is not empty")
                }
            })

            isOneTime.observe(this@AddNoteActivity, {
                if (it == true) {
                    isEveryday.value = false
                    isSomeDay.value = false
                }
            })

            isEveryday.observe(this@AddNoteActivity, {
                if (it == true) {
                    isOneTime.value = false
                    isSomeDay.value = false
                }
            })

            isSomeDay.observe(this@AddNoteActivity, {
                if (it == true) {
                    isOneTime.value = false
                    isEveryday.value = false
                }
            })

            isClose.observe(this@AddNoteActivity, {
                if (it == true) {
                    finish()
                }
            })

            getAllTaskPaged().observe(this@AddNoteActivity, { tasks ->
                if (!tasks.isNullOrEmpty()) {
                    taskId += tasks.size
                }
            })
        }
    }

    private fun setupRemindOneTime() {
        Log.d("--->", "setupRemindOneTime")
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.date.year,
            binding.date.month,
            binding.date.dayOfMonth,
            binding.time.hour,
            binding.time.minute,
            0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        viewModel.remindInMillis = customTime

        val data = Data.Builder().putInt(NOTIFICATION_ID, viewModel.taskId).build()
        val delay = customTime - currentTime
        Log.d("--->", "put id: ${viewModel.taskId}")
        Log.d("--->", "remindInMillis: + ${viewModel.remindInMillis}")

        // Setup worker
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(
            NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork
        ).enqueue()
    }

    private fun setupRemindEveryDay() {
        Log.d("--->", "setupRemindEveryDay")
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.date.year,
            binding.date.month,
            binding.date.dayOfMonth,
            binding.time.hour,
            binding.time.minute,
            0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        viewModel.remindInMillis = customTime

        val data = Data.Builder().putInt(NOTIFICATION_ID, viewModel.taskId).build()
        val delay = customTime - currentTime

        // Setup Worker
        val request =
            PeriodicWorkRequest.Builder(NotifyWorker::class.java, 24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP, request
        )
    }

    private fun setupRemindSomeDay() {
        Log.d("--->", "setupRemindSomeDay")
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.date.year,
            binding.date.month,
            binding.date.dayOfMonth,
            binding.time.hour,
            binding.time.minute,
            0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()
        Log.d("--->", "setUpRemindByDay currentTime: $currentTime")
        Log.d("--->", "setUpRemindByDay customTime: $customTime")

        viewModel.remindInMillis = customTime
        val delay = customTime - currentTime

        if (viewModel.isSunSelected.value == true) {
            Log.d("--->", "setupRemind in sunday")
            setUpRemindByDay(Calendar.SUNDAY, delay)
        }
        if (viewModel.isMonSelected.value == true) {
            Log.d("--->", "setupRemind in monday")
            setUpRemindByDay(Calendar.MONDAY, delay)
        }

        if (viewModel.isTueSelected.value == true) {
            Log.d("--->", "setupRemind in tuesday")
            setUpRemindByDay(Calendar.TUESDAY, delay)
        }

        if (viewModel.isWendSelected.value == true) {
            Log.d("--->", "setupRemind in wednesday")
            setUpRemindByDay(Calendar.WEDNESDAY, delay)
        }

        if (viewModel.isThursSelected.value == true) {
            Log.d("--->", "setupRemind in thursday")
            setUpRemindByDay(Calendar.THURSDAY, delay)
        }

        if (viewModel.isFriSelected.value == true) {
            Log.d("--->", "setupRemind in friday")
            setUpRemindByDay(Calendar.FRIDAY, delay)
        }

        if (viewModel.isSatSelected.value == true) {
            Log.d("--->", "setupRemind in saturday")
            setUpRemindByDay(Calendar.SATURDAY, delay)
        }
    }

    private fun setUpRemindByDay(day: Int, delay: Long) {
        val c = Calendar.getInstance()
        val dayOfWeek = c[Calendar.DAY_OF_WEEK]
        var dayGap = 0

        if (dayOfWeek < day) {
            dayGap = day - dayOfWeek
        }

        if (dayOfWeek > day) {
            dayGap = 7 + day - dayOfWeek
        }

        val data = Data.Builder().putInt(NOTIFICATION_ID, viewModel.taskId).build()
        val totalDelay = delay + dayGap * 24 * 60 * 60 * 1000

        Log.d("--->", "setUpRemindByDay delay: $totalDelay")

        // Setup Worker
        val request =
            PeriodicWorkRequest.Builder(NotifyWorker::class.java, 24 * 7, TimeUnit.HOURS)
                .setInitialDelay(totalDelay, TimeUnit.MILLISECONDS)
                .setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP, request
        )
    }
}
