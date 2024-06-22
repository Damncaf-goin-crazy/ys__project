package com.example.playgroundyandexschool.utils.viewModels


import androidx.lifecycle.ViewModel
import com.example.playgroundyandexschool.utils.TodoItemsRepository
import com.example.playgroundyandexschool.utils.classes.Priority
import com.example.playgroundyandexschool.utils.classes.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class EditTodoVIewModel : ViewModel() {
    private var currentItem: TodoItem? = null
    private var temporaryItem: TodoItem? = null
    private val repository = TodoItemsRepository
    var isNewItem = false


    private val _priorityText = MutableStateFlow("")
    val priorityText: StateFlow<String> get() = _priorityText


    fun setId(id: String) {
        if (currentItem == null) {
            currentItem = repository.getTodoItem(id)
            temporaryItem = currentItem?.copy()
            _priorityText.value = getPriorityText()
        }
    }

    fun createItem() {
        if (currentItem == null) {
            currentItem = TodoItem.empty()
            temporaryItem = currentItem?.copy()
            _priorityText.value = getPriorityText()
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

    fun changePriority(text: String) {
        when (text) {
            "!! Высокая" -> temporaryItem?.priority = Priority.HIGH
            "Низкая" -> temporaryItem?.priority = Priority.LOW
            else -> temporaryItem?.priority = Priority.NO
        }
        _priorityText.value = getPriorityText()

    }

    fun getPriorityText(): String {
        return when (temporaryItem?.priority) {
            Priority.HIGH -> "!! Высокая"
            Priority.LOW -> "Низкая"
            else -> "Нет"
        }
    }

    fun getDeadline(): Long? {
        return temporaryItem?.deadline
    }


}