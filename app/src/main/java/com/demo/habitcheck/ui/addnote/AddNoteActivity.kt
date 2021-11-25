package com.demo.habitcheck.ui.addnote

import android.os.Bundle
import androidx.work.*
import com.demo.habitcheck.databinding.ActivityAddNoteBinding
import com.demo.habitcheck.service.NotifyWorker
import com.demo.habitcheck.service.NotifyWorker.Companion.NOTIFICATION_ID
import com.demo.habitcheck.service.NotifyWorker.Companion.NOTIFICATION_WORK
import com.demo.habitcheck.utils.UtilExtensions.myToast
import com.demo.habitcheck.utils.UtilExtensions.updateDayUI
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import android.util.Log
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

        val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
        val delay = customTime - currentTime

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

        val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
        val delay = customTime - currentTime

        // Setup Worker
        val request =
            PeriodicWorkRequest.Builder(NotifyWorker::class.java, 15, TimeUnit.MINUTES)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP, request
        )
    }
}
