package com.demo.habitcheck.ui.addnote

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.demo.habitcheck.databinding.ActivityAddNoteBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AddNoteActivity : DaggerAppCompatActivity() {
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
    }

    private fun initData() {}

    private fun observeField() {}

}
