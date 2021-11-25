package com.demo.habitcheck.ui.notedone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class NoteDoneViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Note Done Fragment"
    }
    val text: LiveData<String> = _text
}