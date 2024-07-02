package com.example.playgroundyandexschool.utils.viewModels


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.utils.TodoItemsRepository
import com.example.playgroundyandexschool.utils.classes.Priority
import com.example.playgroundyandexschool.utils.classes.TodoItem
import com.example.playgroundyandexschool.utils.classes.getText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EditTodoViewModel : ViewModel() {
    private var currentItem: TodoItem? = null
    private var temporaryItem: TodoItem? = null
    private val repository = TodoItemsRepository
    var isNewItem = false

    private val _priorityText = MutableStateFlow("")
    val priorityText: StateFlow<String> get() = _priorityText

    fun setId(context: Context, id: String) {
        viewModelScope.launch(Dispatchers.IO){
            if (currentItem == null) {
                currentItem = repository.getTodoItem(id)
                temporaryItem = currentItem?.copy()
                _priorityText.value = getPriorityText(context)
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

    fun deleteItem() {
        viewModelScope.launch(Dispatchers.IO){
            currentItem?.let { repository.removeTodoItem(it.id) }

        }
    }

    fun getItem(): TodoItem {
        return currentItem ?: TodoItem.empty()
    }

//    fun saveItem(){
//        viewModelScope.launch(Dispatchers.IO){
//            temporaryItem?.let {
//                currentItem = it
//                repository.saveTodoItem(currentItem)
//            }
//        }
//    }

fun saveItem(): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        try {
            temporaryItem?.let {
                currentItem = it
                repository.saveTodoItem(currentItem)
            }
        } catch (e: Exception) {
            Log.e("EditTodoViewModel", "Exception: ${e.message}")
        }
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