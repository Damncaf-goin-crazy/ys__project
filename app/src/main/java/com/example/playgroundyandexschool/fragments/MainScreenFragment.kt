package com.example.playgroundyandexschool.fragments

import com.example.playgroundyandexschool.utils.classes.SwipeToDeleteCallback
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
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.databinding.FragmentMainScreenBinding
import com.example.playgroundyandexschool.utils.TodoItemsAdapter
import com.example.playgroundyandexschool.utils.viewModels.MainViewModel
import kotlinx.coroutines.launch


class MainScreenFragment : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val adapter = TodoItemsAdapter(
        updateFunction = { viewModel.updateFilteredList() },
        onItemClicked = { itemId ->
            val action = MainScreenFragmentDirections.mainToEdit(itemId)
            findNavController().navigate(action)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        viewLifecycleOwner.lifecycleScope.launch {
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
        val swipeToDeleteCallback = SwipeToDeleteCallback(
            context = requireContext(),
            adapter = adapter,
            deleteItem = { viewModel.deleteItem(it) },
            changeDone = { item, isDone -> viewModel.changeDone(item, isDone) }
        )
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTodoList)
    }
}