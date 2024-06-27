package com.example.playgroundyandexschool.utils

import com.example.playgroundyandexschool.utils.classes.Priority
import com.example.playgroundyandexschool.utils.classes.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object TodoItemsRepository {

    private val todoItemsList: MutableStateFlow<MutableList<TodoItem>> =
        MutableStateFlow(mutableListOf())

    var previousListSize = 0

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val list = mutableListOf<TodoItem>()

            // Item 1
            val item1 = TodoItem.empty().copy(
                text = "Помыть посуду",
                priority = Priority.HIGH,
                deadline = 1719435600000L
            )
            list.add(item1)

            // Item 2
            val item2 = TodoItem.empty().copy(
                text = "Сходить в магазин",
                priority = Priority.NO,
                deadline = 1719435600000L
            )
            list.add(item2)

            // Item 3
            val item3 = TodoItem.empty().copy(
                text = "Попробуй отметить Done правым свайпом, а потом удалить левым",
                priority = Priority.LOW,
                deadline = 1719435600000L
            )
            list.add(item3)

            // Item 4
            val item4 = TodoItem.empty().copy(
                text = "Попробуй поменять дедлайн",
                priority = Priority.NO,
                deadline = null
            )
            list.add(item4)

            // Item 5
            val item5 = TodoItem.empty().copy(
                text = "Начать бегать по утрам",
                priority = Priority.LOW,
                deadline = 1654416000000L
            )
            list.add(item5)

            // Item 6
            val item6 = TodoItem.empty().copy(
                text = "Длинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный" +
                        " текстДлинный текстДлинный текстДлинный текстДлинный текстДлинный текст",
                priority = Priority.HIGH,
                deadline = 1719435600000L
            )
            list.add(item6)

            // Item 7
            val item7 = TodoItem.empty().copy(
                text = "Отправить домашку",
                priority = Priority.LOW,
                deadline = null
            )
            list.add(item7)

            // Item 8
            val item8 = TodoItem.empty().copy(
                text = "Оплатить счета",
                priority = Priority.NO,
                deadline = 1719435600000L
            )
            list.add(item8)

            // Item 9
            val item9 = TodoItem.empty().copy(
                text = "Сходить в зал",
                priority = Priority.HIGH,
                deadline = 1719435600000L
            )
            list.add(item9)

            // Item 10
            val item10 = TodoItem.empty().copy(
                text = "Прибрать в квартире",
                priority = Priority.LOW,
                deadline = null
            )
            list.add(item10)
            todoItemsList.value = list
        }
    }


    fun getTodoItems(): StateFlow<List<TodoItem>> {
        return todoItemsList.asStateFlow()
    }

    fun getTodoItem(id: String): TodoItem? {
        val item = todoItemsList.value.find { it.id == id }
        return item
    }

    fun getItemCount(): Int {
        val currentList = todoItemsList.value.toMutableList()
        return currentList.size
    }

    fun saveTodoItem(todoItem: TodoItem?) {
        if (todoItem == null) return
        CoroutineScope(Dispatchers.IO).launch {
            val currentList = todoItemsList.value.toMutableList()
            previousListSize = currentList.size
            val index = currentList.indexOfFirst { it.id == todoItem.id }
            if (index == -1) {
                currentList.add(0, todoItem)
            } else {
                currentList[index] = todoItem
            }
            todoItemsList.value = currentList
        }
    }

    fun removeTodoItem(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentList = todoItemsList.value.toMutableList()
            previousListSize = currentList.size
            val itemToRemove = currentList.find { it.id == id }
            if (itemToRemove != null) {
                currentList.remove(itemToRemove)
                todoItemsList.value = currentList
            }
        }
    }
}