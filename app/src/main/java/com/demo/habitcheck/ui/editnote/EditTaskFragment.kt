package com.demo.habitcheck.ui.editnote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.FragmentEditTaskBinding
import com.demo.habitcheck.service.NotifyWorker
import com.demo.habitcheck.utils.UtilExtensions
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
                    //handle later
                    //setupRemind()

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

    private fun setupRemind() {
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

        val data = Data.Builder().putInt(NotifyWorker.NOTIFICATION_ID, 0).build()
        val delay = customTime - currentTime
        scheduleOneTimeRemind(delay, data)
    }

    private fun scheduleOneTimeRemind(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.beginUniqueWork(
            NotifyWorker.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork
        ).enqueue()
    }
}