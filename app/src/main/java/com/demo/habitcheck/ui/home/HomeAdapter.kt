package com.demo.habitcheck.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.databinding.ItemTaskBinding

class HomeAdapter(private val listener: (Task) -> Unit, val deleteTask: (Task) -> Unit) :
    PagedListAdapter<Task, HomeAdapter.TaskViewHolder>(DiffUtilTask()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindItem(it, listener, deleteTask) }
    }

    class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(task: Task, listener: (Task) -> Unit, deleteTask: (Task) -> Unit) {
            binding.apply {
                this.task = task
                root.setOnClickListener { listener.invoke(task) }
                btnDelete.setOnClickListener {
                    deleteTask.invoke(task)
                }
            }
        }
    }

    private class DiffUtilTask : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return newItem == oldItem
        }
    }
}