package com.demo.habitcheck.ui.editnote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.FragmentEditTaskBinding
import com.demo.habitcheck.service.NotifyWorker
import com.demo.habitcheck.utils.UtilExtensions.mySnackbar
import com.demo.habitcheck.utils.UtilExtensions.updateDayUI
import dagger.android.support.DaggerFragment
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EditTaskFragment : DaggerFragment() {
    @Inject
    lateinit var editTaskViewModel: EditTaskViewModel
    private lateinit var binding: FragmentEditTaskBinding

    companion object {
        const val TASK_ARG = "TASK_ARG"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_task, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        observeField()
    }

    private fun initView() {
        binding.apply {
            viewModel = editTaskViewModel
            lifecycleOwner = this@EditTaskFragment
            executePendingBindings()
        }
    }

    private fun initData() {
        editTaskViewModel.apply {
            arguments?.let {
                task.value = it.getParcelable(TASK_ARG)
                editTaskViewModel.setUpData()
            }

            progressText.observe(viewLifecycleOwner, {
                binding.txtProgress.text = it
            })
        }

        setUpTimeDatePicker()

        binding.apply {
            sbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    editTaskViewModel.progressText.value = "$progress/100"
                    editTaskViewModel.progress.value = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }
    }

    private fun observeField() {
        editTaskViewModel.apply {
            binding.apply {
                isMonSelected.observe(viewLifecycleOwner, {
                    updateDayUI(requireContext(), listDay.txtMonday, it ?: false)
                })
                isTueSelected.observe(viewLifecycleOwner, {
                    updateDayUI(requireContext(), listDay.txtTuesday, it ?: false)
                })
                isWendSelected.observe(viewLifecycleOwner, {
                    updateDayUI(requireContext(), listDay.txtWednesday, it ?: false)
                })
                isThursSelected.observe(viewLifecycleOwner, {
                    updateDayUI(requireContext(), listDay.txtThursday, it ?: false)
                })
                isFriSelected.observe(viewLifecycleOwner, {
                    updateDayUI(requireContext(), listDay.txtFriday, it ?: false)
                })
                isSatSelected.observe(viewLifecycleOwner, {
                    updateDayUI(requireContext(), listDay.txtSaturday, it ?: false)
                })
                isSunSelected.observe(viewLifecycleOwner, {
                    updateDayUI(requireContext(), listDay.txtSunday, it ?: false)
                })
            }

            isSave.observe(viewLifecycleOwner, {
                if (it == true) {
                    if (isEditTime.value == true) {
                        if (isOneTime.value == true) {
                            setupRemindOneTime()
                        }
                        if (isEveryday.value == true) {
                            setupRemindEveryDay()
                        }
                        if (isSomeDay.value == true) {
                            setupRemindSomeDay()
                        }
                    }
                    saveTask(binding.edtTitle.text.toString(), binding.edtDesc.text.toString())
                }
            })

            isSaveTaskResult.observe(viewLifecycleOwner, {
                if (it == true) {
                    mySnackbar(binding.root, "Save task success")
                    findNavController().popBackStack()
                } else {
                    mySnackbar(binding.root, "Title and des is not empty")
                }
            })

            isEditTime.observe(viewLifecycleOwner, {
                if (it != null) {
                    setDisableView(binding.redmindView, it)
                }
            })
        }
    }

    private fun setUpTimeDatePicker() {
        Log.d("--->", "remindInMillis: + ${editTaskViewModel.task.value?.remindInMillis}")
        val calendar = Calendar.getInstance()
        editTaskViewModel.task.value?.remindInMillis?.let {
            calendar.timeInMillis = it
        }

        // Set old time
        editTaskViewModel.remindInMillis = calendar.timeInMillis


        binding.timePicker.apply {
            currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            currentMinute = calendar.get(Calendar.MINUTE)
        }


        binding.datePicker.apply {
            init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                null
            )
        }
    }

    private fun setupRemindOneTime() {
        Log.d("--->", "setupRemindOneTime")
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth,
            binding.timePicker.hour,
            binding.timePicker.minute,
            0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        editTaskViewModel.remindInMillis = customTime

        val data =
            Data.Builder().putInt(NotifyWorker.NOTIFICATION_ID, editTaskViewModel.task.value?.id!!)
                .build()
        val delay = customTime - currentTime
        Log.d("--->", "put id: ${editTaskViewModel.task.value?.id!!}")
        Log.d("--->", "remindInMillis: + ${editTaskViewModel.remindInMillis}")

        // Setup worker
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.beginUniqueWork(
            NotifyWorker.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork
        ).enqueue()
    }

    private fun setupRemindEveryDay() {
        Log.d("--->", "setupRemindEveryDay")
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth,
            binding.timePicker.hour,
            binding.timePicker.minute,
            0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        editTaskViewModel.remindInMillis = customTime

        val data =
            Data.Builder().putInt(NotifyWorker.NOTIFICATION_ID, editTaskViewModel.task.value?.id!!)
                .build()
        val delay = customTime - currentTime

        // Setup Worker
        val request =
            PeriodicWorkRequest.Builder(NotifyWorker::class.java, 24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.enqueueUniquePeriodicWork(
            NotifyWorker.NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP, request
        )
    }

    private fun setupRemindSomeDay() {
        Log.d("--->", "setupRemindSomeDay")
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth,
            binding.timePicker.hour,
            binding.timePicker.minute,
            0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()
        Log.d("--->", "setUpRemindByDay currentTime: $currentTime")
        Log.d("--->", "setUpRemindByDay customTime: $customTime")

        editTaskViewModel.remindInMillis = customTime
        val delay = customTime - currentTime

        if (editTaskViewModel.isSunSelected.value == true) {
            Log.d("--->", "setupRemind in sunday")
            setUpRemindByDay(Calendar.SUNDAY, delay)
        }
        if (editTaskViewModel.isMonSelected.value == true) {
            Log.d("--->", "setupRemind in monday")
            setUpRemindByDay(Calendar.MONDAY, delay)
        }

        if (editTaskViewModel.isTueSelected.value == true) {
            Log.d("--->", "setupRemind in tuesday")
            setUpRemindByDay(Calendar.TUESDAY, delay)
        }

        if (editTaskViewModel.isWendSelected.value == true) {
            Log.d("--->", "setupRemind in wednesday")
            setUpRemindByDay(Calendar.WEDNESDAY, delay)
        }

        if (editTaskViewModel.isThursSelected.value == true) {
            Log.d("--->", "setupRemind in thursday")
            setUpRemindByDay(Calendar.THURSDAY, delay)
        }

        if (editTaskViewModel.isFriSelected.value == true) {
            Log.d("--->", "setupRemind in friday")
            setUpRemindByDay(Calendar.FRIDAY, delay)
        }

        if (editTaskViewModel.isSatSelected.value == true) {
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

        val data =
            Data.Builder().putInt(NotifyWorker.NOTIFICATION_ID, editTaskViewModel.task.value?.id!!)
                .build()
        val totalDelay = delay + dayGap * 24 * 60 * 60 * 1000

        Log.d("--->", "setUpRemindByDay delay: $totalDelay")

        // Setup Worker
        val request =
            PeriodicWorkRequest.Builder(NotifyWorker::class.java, 24 * 7, TimeUnit.HOURS)
                .setInitialDelay(totalDelay, TimeUnit.MILLISECONDS)
                .setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.enqueueUniquePeriodicWork(
            NotifyWorker.NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP, request
        )
    }

    private fun setDisableView(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (idx in 0 until view.childCount) {
                setDisableView(view.getChildAt(idx), enabled)
            }
        }
    }
}