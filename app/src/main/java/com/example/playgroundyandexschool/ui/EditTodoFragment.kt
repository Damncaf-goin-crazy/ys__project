package com.example.playgroundyandexschool.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playgroundyandexschool.ui.editTodoFragmentCompose.DeadlineLayout
import com.example.playgroundyandexschool.ui.editTodoFragmentCompose.DeleteButton
import com.example.playgroundyandexschool.ui.editTodoFragmentCompose.InputTextField
import com.example.playgroundyandexschool.ui.editTodoFragmentCompose.PriorityMenu
import com.example.playgroundyandexschool.ui.editTodoFragmentCompose.TopToolbar
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme
import com.example.playgroundyandexschool.utils.viewModels.EditTodoViewModel


class EditTodoFragment : Fragment() {
    private val editViewModel: EditTodoViewModel by viewModels()
    private val navArgs: EditTodoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                AppTodoTheme {
                    EditTodoScreen(editViewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val itemId = navArgs.id
        if (itemId == null) {
            editViewModel.createItem(requireContext())
        } else {
            editViewModel.setId(requireContext(), itemId)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTodoScreen(editViewModel: EditTodoViewModel) {
        val scrollState = rememberScrollState()

        var text by remember { mutableStateOf(editViewModel.getItem().text) }

        Scaffold(
            modifier = Modifier
                .nestedScroll(TopAppBarDefaults.pinnedScrollBehavior().nestedScrollConnection),
            topBar = {
                TopToolbar(
                    navController = findNavController(),
                    onSave = { editViewModel.saveItem() },
                    scrollState = scrollState
                )
            },
            ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxHeight()
                    .verticalScroll(scrollState)
                    .background(AppTodoTheme.colors.backPrimary)
            ) {
                InputTextField(text = text,
                    onChanged = { newText ->
                        text = newText
                        editViewModel.changeText(newText)  // Also update the ViewModel
                    })
                Spacer(modifier = Modifier.height(12.dp))
                PriorityMenu(
                    changePriority = { priority ->
                        editViewModel.changePriority(
                            requireContext(),
                            priority
                        )
                    },
                    priorityTextFlow = editViewModel.priorityText,
                )
                HorizontalDivider(thickness = 1.dp, color = AppTodoTheme.colors.supportSeparator)
                Spacer(modifier = Modifier.height(12.dp))
                DeadlineLayout(
                    changeDeadline = { deadline -> editViewModel.changeDeadline(deadline) },
                    getDeadline = { editViewModel.getDeadline() })
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(thickness = 1.dp, color = AppTodoTheme.colors.supportSeparator)
                DeleteButton(
                    onClick = {
                        editViewModel.deleteItem()
                        findNavController().popBackStack()
                    },
                    state = !editViewModel.isNewItem
                )

            }
        }
    }
}
