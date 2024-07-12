package com.example.playgroundyandexschool.ui.viewModels


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.data.TodoItemsRepository
import com.example.playgroundyandexschool.ui.models.Priority
import com.example.playgroundyandexschool.ui.models.TodoItem
import com.example.playgroundyandexschool.ui.models.getText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Класс EditTodoViewModel представляет ViewModel для редактирования или создания задачи.
 */

@HiltViewModel
class EditTodoViewModel @Inject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    private var currentItem: TodoItem? = null
    private var temporaryItem: TodoItem? = null
    var isNewItem = false

    private val _priorityText = MutableStateFlow("")
    val priorityText: StateFlow<String> get() = _priorityText

    fun setId(context: Context, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentItem == null) {
                currentItem = repository.getTodoItem(id)
                temporaryItem = currentItem?.copy()
                _priorityText.value = getPriorityText(context)
            }
        }
    }

    fun deleteItem() {
        viewModelScope.launch(Dispatchers.IO) {
            currentItem?.let {
                repository.removeTodoItem(it.id)
            }
        }
    }

    fun saveItem(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            temporaryItem?.let {
                currentItem = it
                repository.saveTodoItem(currentItem)
            }
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

    fun getItem(): TodoItem {
        return currentItem ?: TodoItem.empty()
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