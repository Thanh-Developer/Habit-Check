package com.demo.habitcheck.ui.addnote

import android.os.Bundle
import androidx.activity.viewModels
import com.demo.habitcheck.databinding.ActivityAddNoteBinding
import dagger.android.support.DaggerAppCompatActivity

class AddNoteActivity : DaggerAppCompatActivity() {
    private val viewModel: AddNoteViewModel by viewModels()
    private lateinit var binding: ActivityAddNoteBinding

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
