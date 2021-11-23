package com.demo.habitcheck.ui.editnote

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.FragmentEditTaskBinding
import com.demo.habitcheck.ui.addnote.TimePickerFragment
import com.demo.habitcheck.utils.DateUtils
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

class EditTaskFragment : DaggerFragment(), TimePickerDialog.OnTimeSetListener {
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = editTaskViewModel
        editTaskViewModel.apply {
            arguments?.let {
                task.value = it.getParcelable(TASK_ARG)
                editTaskViewModel.setUpData()
            }
            isMonSelected.observe(viewLifecycleOwner, {
                updateTextUi(binding.dayList.txtMonday, it ?: false)
            })
            isTueSelected.observe(viewLifecycleOwner, {
                updateTextUi(binding.dayList.txtTuesday, it ?: false)
            })
            isWendSelected.observe(viewLifecycleOwner, {
                updateTextUi(binding.dayList.txtWednesday, it ?: false)
            })
            isThursSelected.observe(viewLifecycleOwner, {
                updateTextUi(binding.dayList.txtThursday, it ?: false)
            })
            isFriSelected.observe(viewLifecycleOwner, {
                updateTextUi(binding.dayList.txtFriday, it ?: false)
            })
            isSatSelected.observe(viewLifecycleOwner, {
                updateTextUi(binding.dayList.txtSaturday, it ?: false)
            })
            isSunSelected.observe(viewLifecycleOwner, {
                updateTextUi(binding.dayList.txtSunday, it ?: false)
            })
            progressText.observe(viewLifecycleOwner, {
                binding.txtProgress.text = it
            })
            remindTime.observe(viewLifecycleOwner, {
                binding.txtRemindTime.text = it
            })
            isSaveTaskResult.observe(viewLifecycleOwner, {
                if (it == true) {
                    Toast.makeText(requireContext(), "Save task success", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Title and des is not empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
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

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })
            txtRemindTime.setOnClickListener {
                editTaskViewModel.task.value?.remindTime?.split(":")?.let {
                    val timePicker = TimePickerFragment(
                        it[0].toInt(), it[1].toInt(), this@EditTaskFragment
                    )
                    timePicker.show(parentFragmentManager, "time_picker")
                }
            }
            btnSave.setOnClickListener {
                editTaskViewModel.saveTask(edtTitle.text.toString(), edtDesc.text.toString())
            }
        }
    }

    private fun updateTextUi(textView: AppCompatTextView, isSelected: Boolean) {
        if (isSelected) {
            textView.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected)
            textView.setTextColor(resources.getColor(R.color.white, null))
        } else {
            textView.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_un_selected)
            textView.setTextColor(resources.getColor(R.color.black, null))
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        editTaskViewModel.remindTime.value =
            DateUtils.convertDate(calendar.timeInMillis.toString(), "HH:mm")
    }
}