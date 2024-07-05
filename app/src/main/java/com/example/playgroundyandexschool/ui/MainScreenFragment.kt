package com.example.playgroundyandexschool.ui

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
import com.example.playgroundyandexschool.ui.models.UiContent
import com.example.playgroundyandexschool.ui.models.UiContent.UiState
import com.example.playgroundyandexschool.ui.recycler.SwipeToDeleteCallback
import com.example.playgroundyandexschool.ui.recycler.TodoItemsAdapter
import com.example.playgroundyandexschool.ui.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * Фрагмент MainScreenFragment отображает основной экран тудулиста.
 */
class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private val adapter = TodoItemsAdapter(
        changeDoneFunction = { item, doneState ->
            viewModel.changeDone(item, doneState)
        },
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeViewModel()
        registerEvents()
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

    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateMainScreen.collect { uiState ->
                observeVisibilityChanges(uiState)
                observeListChanges(uiState)
                observeCompletedCounterChanges(uiState)
                observeNetworkStatusChanges(uiState)
                handleUiStateChanges(uiState)
            }
        }
    }

    private fun observeVisibilityChanges(uiState: UiContent) {
        val iconRes = if (uiState.todosVisible) R.drawable.visibility_off else R.drawable.visibility
        binding.ivVisibility.setImageResource(iconRes)
    }

    private fun observeListChanges(uiState: UiContent) {
        val currentSize = adapter.itemCount
        adapter.submitList(uiState.filteredList) {
            if (currentSize < adapter.itemCount) {
                binding.rvTodoList.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun observeCompletedCounterChanges(uiState: UiContent) {
        binding.tvCompletedCounter.text = getString(R.string.completed, uiState.numDone)
    }

    private fun observeNetworkStatusChanges(uiState: UiContent) {
        val iconNetwork = if (uiState.isConnected) {
            R.drawable.refresh_online
        } else {
            R.drawable.refresh_offline
        }
        binding.ivNetworkStatusRefresh.setImageResource(iconNetwork)

        binding.ivNetworkStatusRefresh.setOnClickListener {
            if (uiState.isConnected) {
                viewModel.updateItemsData()
            } else {
                val text = getString(R.string.offline_message)
                val duration = Snackbar.LENGTH_SHORT
                val snackBar = Snackbar.make(binding.root, text, duration)
                snackBar.show()
            }
        }
    }

    private fun handleUiStateChanges(uiState: UiContent) {
        when (uiState.uiState) {
            is UiState.Loading -> {
                binding.rvTodoList.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }

            is UiState.Error -> {
                binding.rvTodoList.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                val text = uiState.uiState.errorMessage
                val duration = Snackbar.LENGTH_SHORT
                val snackbar = Snackbar.make(binding.root, text, duration)
                snackbar.show()
            }

            is UiState.Content -> {
                binding.rvTodoList.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
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


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}