package com.example.playgroundyandexschool.ui.recycler

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.ui.models.TodoItem
import com.example.playgroundyandexschool.utils.ViewUtils
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

/**
 * Класс SwipeToDeleteCallback обеспечивает функциональность свайпа для удаления элементов в RecyclerView или для смены стейта выполнения.
 */
class SwipeToDeleteCallback(
    private val context: Context,
    private val adapter: TodoItemsAdapter,
    private val deleteItem: (item: TodoItem) -> Unit,
    private val changeDone: (item: TodoItem, isDone: Boolean) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = adapter.getElement(position)
        when (direction) {
            ItemTouchHelper.LEFT -> {
                deleteItem(item)
                adapter.notifyItemChanged(position)
            }

            ItemTouchHelper.RIGHT -> {
                changeDone(item, !item.isCompleted)
                adapter.notifyItemChanged(position)
            }
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator
            .Builder(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
            .addBackgroundColor(
                ViewUtils.resolveColorAttr(context, R.attr.back_secondary)
            )
            .addSwipeRightBackgroundColor(
                ViewUtils.resolveColorAttr(context, R.attr.color_green)
            )
            .addSwipeLeftBackgroundColor(
                ViewUtils.resolveColorAttr(context, R.attr.color_red)
            )
            .addSwipeRightActionIcon(R.drawable.done)
            .addSwipeLeftActionIcon(R.drawable.delete_white)
            .create()
            .decorate()
        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}