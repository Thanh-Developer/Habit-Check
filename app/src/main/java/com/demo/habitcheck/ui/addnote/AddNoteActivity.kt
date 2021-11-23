package com.demo.habitcheck.ui.addnote

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.ActivityAddNoteBinding
import com.demo.habitcheck.utils.DateUtils
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject

class AddNoteActivity : DaggerAppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    @Inject
    lateinit var viewModel: AddNoteViewModel
    private lateinit var binding: ActivityAddNoteBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        observeField()
    }

    private fun initView() {
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            txtRemindTime.setOnClickListener {
                val timePicker = TimePickerFragment(listener = this@AddNoteActivity)
                timePicker.show(supportFragmentManager, "time picker")
            }
        }
    }

    private fun initData() {
        viewModel.apply {
            binding.apply {
                listDay.txtMonday.setOnClickListener {
                    onMondaySelected()
                }
                listDay.txtTuesday.setOnClickListener {
                    onTuesdaySelected()
                }
                listDay.txtWednesday.setOnClickListener {
                    onWednesdaySelected()
                }
                listDay.txtThursday.setOnClickListener {
                    onThursSelected()
                }
                listDay.txtFriday.setOnClickListener {
                    onFridaySelected()
                }
                listDay.txtSaturday.setOnClickListener {
                    onSaturdaySelected()
                }
                listDay.txtSunday.setOnClickListener {
                    onSundaySelected()
                }
                toolBar.btnDone.setOnClickListener {
                    saveTask(edtTitle.text.toString(), edtDesc.text.toString())
                }
                toolBar.btnClose.setOnClickListener {
                    finish()
                }
            }
        }
    }

    private fun observeField() {
        viewModel.apply {
            remindTime.observe(this@AddNoteActivity, {
                binding.txtRemindTime.text = it
            })
            isMonSelected.observe(this@AddNoteActivity, {
                updateTextUi(binding.listDay.txtMonday, it ?: false)
            })
            isTueSelected.observe(this@AddNoteActivity, {
                updateTextUi(binding.listDay.txtTuesday, it ?: false)
            })
            isWendSelected.observe(this@AddNoteActivity, {
                updateTextUi(binding.listDay.txtWednesday, it ?: false)
            })
            isThursSelected.observe(this@AddNoteActivity, {
                updateTextUi(binding.listDay.txtThursday, it ?: false)
            })
            isFriSelected.observe(this@AddNoteActivity, {
                updateTextUi(binding.listDay.txtFriday, it ?: false)
            })
            isSatSelected.observe(this@AddNoteActivity, {
                updateTextUi(binding.listDay.txtSaturday, it ?: false)
            })
            isSunSelected.observe(this@AddNoteActivity, {
                updateTextUi(binding.listDay.txtSunday, it ?: false)
            })
            isSaveTaskResult.observe(this@AddNoteActivity, {
                if (it == true) {
                    Toast.makeText(applicationContext, "Save task success", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Title and des is not empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun updateTextUi(textView: AppCompatTextView, isSelected: Boolean) {
        if (isSelected) {
            textView.background =
                ContextCompat.getDrawable(applicationContext, R.drawable.bg_selected)
            textView.setTextColor(resources.getColor(R.color.white, null))
        } else {
            textView.background =
                ContextCompat.getDrawable(applicationContext, R.drawable.bg_un_selected)
            textView.setTextColor(resources.getColor(R.color.black, null))
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        viewModel.remindTime.value =
            DateUtils.convertDate(calendar.timeInMillis.toString(), "HH:mm")
    }
}
