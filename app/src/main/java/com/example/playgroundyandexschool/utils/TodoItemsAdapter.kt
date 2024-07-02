package com.example.playgroundyandexschool.utils

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.databinding.TodoItemBinding
import com.example.playgroundyandexschool.utils.classes.Priority
import com.example.playgroundyandexschool.utils.classes.TodoItem
import com.example.playgroundyandexschool.utils.classes.ViewUtils

class TodoItemsAdapter(
    private val updateFunction: () -> Unit,
    private val onItemClicked: (String) -> Unit
) :
    ListAdapter<TodoItem, TodoItemsAdapter.TodoItemsViewHolder>(TodoItemDiffCallback()) {

    inner class TodoItemsViewHolder(private val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(todoItem: TodoItem) {
            itemView.setOnClickListener {
                onItemClicked(todoItem.id)
            }

            binding.tvTodoDeadline.text = todoItem.deadline.toString()
            binding.tvTodo.text = todoItem.text

            when (todoItem.priority) {
                Priority.HIGH -> {
                    binding.ivPriority.setImageResource(R.drawable.priority_high)
                    binding.ivPriority.contentDescription =
                        binding.root.context.getString(R.string.high_priority)
                }

                Priority.LOW -> {
                    binding.ivPriority.setImageResource(R.drawable.priority_low)
                    binding.ivPriority.contentDescription =
                        binding.root.context.getString(R.string.low_priority)
                }

                else -> {
                    binding.ivPriority.setImageResource(0)
                    binding.ivPriority.contentDescription =
                        binding.root.context.getString(R.string.no_priority)
                }
            }

            if (todoItem.deadline != null) {
                binding.tvTodoDeadline.isVisible = true
                binding.tvTodoDeadline.text = ViewUtils.convertMillisToDateString(todoItem.deadline)
            } else {
                binding.tvTodoDeadline.isVisible = false
            }

            binding.ivCheckbox.setImageResource(
                if (todoItem.isCompleted) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked_normal
            )
            if (todoItem.isCompleted) {
                binding.tvTodo.paintFlags = binding.tvTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvTodo.paintFlags =
                    binding.tvTodo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            binding.ivCheckbox.setOnClickListener {
                todoItem.isCompleted = !todoItem.isCompleted

                binding.ivCheckbox.setImageResource(
                    if (todoItem.isCompleted) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked_normal
                )
                if (todoItem.isCompleted) {
                    binding.tvTodo.paintFlags =
                        binding.tvTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    binding.tvTodo.paintFlags =
                        binding.tvTodo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                updateFunction()
            }
        }
    }

    override fun onBindViewHolder(holder: TodoItemsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemsViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemsViewHolder(binding)
    }

    fun getElement(position: Int): TodoItem {
        return currentList[position]
    }

    companion object {
        class TodoItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
            override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
