package com.example.playgroundyandexschool.utils.viewModels


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.TodoItemsRepository
import com.example.playgroundyandexschool.utils.classes.Priority
import com.example.playgroundyandexschool.utils.classes.TodoItem
import com.example.playgroundyandexschool.utils.classes.getText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class EditTodoViewModel : ViewModel() {
    private var currentItem: TodoItem? = null
    private var temporaryItem: TodoItem? = null
    private val repository = TodoItemsRepository
    var isNewItem = false

    private val _priorityText = MutableStateFlow("")
    val priorityText: StateFlow<String> get() = _priorityText

    fun setId(context: Context, id: String) {
        if (currentItem == null) {
            currentItem = repository.getTodoItem(id)
            temporaryItem = currentItem?.copy()
            _priorityText.value = getPriorityText(context)
        }
    }

    fun createItem(context: Context) {
        if (currentItem == null) {
            currentItem = TodoItem.empty()
            temporaryItem = currentItem?.copy()
            _priorityText.value = getPriorityText(context)
        }
        isNewItem = true
    }

    fun deleteItem() {
        currentItem?.let { repository.removeTodoItem(it.id) }
    }

    fun getItem(): TodoItem {
        return currentItem ?: TodoItem.empty()
    }

    fun saveItem() {
        temporaryItem?.let {
            currentItem = it
            repository.saveTodoItem(currentItem)
        }
    }

    fun changeText(text: String) {
        temporaryItem?.text = text
    }

    fun changeDeadline(deadline: Long?) {
        temporaryItem?.deadline = deadline
    }

    fun getDeadline(): Long? {
        return temporaryItem?.deadline
    }

    fun changePriority(context: Context, priority: Priority) {
        temporaryItem?.priority = priority
        _priorityText.value = getPriorityText(context)
    }

    private fun getPriorityText(context: Context): String {
        return temporaryItem?.priority?.getText(context) ?: context.getString(R.string.priority_no)
    }

}