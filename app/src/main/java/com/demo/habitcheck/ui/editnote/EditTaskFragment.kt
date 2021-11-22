package com.demo.habitcheck.ui.editnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.demo.habitcheck.R
import com.demo.habitcheck.databinding.FragmentEditTaskBinding
import dagger.android.support.DaggerFragment
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
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTaskViewModel.apply {
            arguments?.let {
                task.value = it.getParcelable(TASK_ARG)
            }
        }
    }
}