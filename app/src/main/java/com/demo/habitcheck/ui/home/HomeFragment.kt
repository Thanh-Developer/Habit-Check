package com.demo.habitcheck.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.FragmentHomeBinding
import com.demo.habitcheck.ui.editnote.EditTaskFragment.Companion.TASK_ARG
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var homeViewModel: HomeViewModel

    lateinit var homeAdapter: HomeAdapter

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
        homeAdapter = HomeAdapter {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, it)
            findNavController().navigate(R.id.nav_edit_task, bundle)
        }
        binding.apply {
            rvTask.apply {
                adapter = homeAdapter
            }
        }
    }

    private fun observeField() {
        homeViewModel.apply {
            getAllNotePaged().observe(viewLifecycleOwner, {
                if (!it.isNullOrEmpty()) {
                    homeAdapter.submitList(it)
                }
            })
        }
    }
}