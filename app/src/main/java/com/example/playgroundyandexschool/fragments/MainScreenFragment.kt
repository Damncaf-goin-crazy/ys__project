package com.example.playgroundyandexschool.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.databinding.FragmentMainScreenBinding
import com.example.playgroundyandexschool.utils.TodoItemsAdapter
import com.example.playgroundyandexschool.utils.classes.ViewUtils
import com.example.playgroundyandexschool.utils.viewModels.MainViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch


class MainScreenFragment : Fragment() {
    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = TodoItemsAdapter(
        function = { viewModel.updateFilteredList() },
        onItemClicked = { itemId ->
            val action = MainScreenFragmentDirections.mainToEdit(itemId)
            findNavController().navigate(action)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
        observeViewModel()
        setupSwipeToDelete()
    }

    private fun init() = with(binding) {
        rvTodoList.adapter = adapter
        rvTodoList.layoutManager = LinearLayoutManager(context)
    }

    private fun registerEvents(): Unit = with(binding) {
        btnAddTodo.setOnClickListener {
            val action = MainScreenFragmentDirections.mainToEdit(null)
            findNavController().navigate(action)
        }

        ivVisibility.setOnClickListener {
            viewModel.toggleVisibility()
        }

    }

    private fun observeViewModel(): Unit = with(binding) {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                val iconRes =
                    if (uiState.todosVisible) R.drawable.visibility else R.drawable.visibility_off
                ivVisibility.setImageResource(iconRes)
                adapter.submitList(uiState.filteredList) {
                    if (viewModel.isNewListBigger()) {
                        binding.rvTodoList.scrollToPosition(0)
                    }
                }
                val completedText = getString(R.string.completed, uiState.numDone)
                tvCompletedCounter.text = completedText
            }
        }
    }


    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
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
                        viewModel.deleteItem(item)
                    }

                    ItemTouchHelper.RIGHT -> {
                        viewModel.changeDone(item, !item.isCompleted)
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
                        ViewUtils.resolveColorAttr(requireContext(), R.attr.back_secondary)
                    )
                    .addSwipeRightBackgroundColor(
                        ViewUtils.resolveColorAttr(requireContext(), R.attr.color_green)
                    )
                    .addSwipeLeftBackgroundColor(
                        ViewUtils.resolveColorAttr(requireContext(), R.attr.color_red)
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

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTodoList)
    }
}