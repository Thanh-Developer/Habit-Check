package com.demo.habitcheck.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.FragmentHomeBinding
import com.demo.habitcheck.ui.editnote.EditTaskFragment.Companion.TASK_ARG
import com.demo.habitcheck.utils.Coroutines
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
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
        homeAdapter = HomeAdapter({
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, it)
            findNavController().navigate(R.id.nav_edit_task, bundle)
        }, {
            WorkManager.getInstance(requireContext())
                .cancelUniqueWork(it.workerId ?: return@HomeAdapter)
            homeViewModel.deleteTask(it)
        })
        binding.apply {
            rvTask.apply {
                adapter = homeAdapter
            }
        }
        binding.cbFilter.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Coroutines.main {
                    homeViewModel.getAllNoteNotDonePaged().collectLatest { pagingData ->
                        homeAdapter.submitData(pagingData)
                    }
                }
            } else {
                Coroutines.main {
                    homeViewModel.getAllNotePaged().collectLatest { pagingData ->
                        homeAdapter.submitData(pagingData)
                    }
                }
            }
        }
    }

    private fun observeField() {
        homeViewModel.apply {
            Coroutines.main {
                getAllNotePaged().collectLatest { pagingData ->
                    homeAdapter.submitData(pagingData)
                }
            }
        }
    }
}