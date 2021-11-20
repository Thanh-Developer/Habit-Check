package com.demo.habitcheck.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.FragmentHomeBinding
import dagger.android.support.DaggerFragment

class HomeFragment : DaggerFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        initView()
        observeField()
        return binding.root
    }

    private fun initView() {
    }

    private fun observeField() {
        homeViewModel.text.observe(viewLifecycleOwner, {
            binding.textHome.text = it
        })
    }
}