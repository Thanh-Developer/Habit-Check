package com.demo.habitcheck.ui.addnote

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(
    private val hour: Int? = null,
    private val minute: Int? = null,
    val listener: TimePickerDialog.OnTimeSetListener? = null
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        return TimePickerDialog(
            context,
            listener,
            hour ?: c[Calendar.HOUR_OF_DAY],
            minute ?: c[Calendar.MINUTE],
            DateFormat.is24HourFormat(
                activity
            )
        )
    }
}