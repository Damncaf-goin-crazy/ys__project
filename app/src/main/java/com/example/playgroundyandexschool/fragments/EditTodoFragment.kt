package com.example.playgroundyandexschool.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playgroundyandexschool.R
import com.example.playgroundyandexschool.databinding.FragmentEditTodoBinding
import com.example.playgroundyandexschool.utils.classes.TodoItem
import com.example.playgroundyandexschool.utils.classes.ViewUtils
import com.example.playgroundyandexschool.utils.viewModels.EditTodoVIewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class EditTodoFragment : Fragment() {
    private var _binding: FragmentEditTodoBinding? = null
    private val binding get() = _binding!!
    private val navArgs: EditTodoFragmentArgs by navArgs()
    private val editViewModel: EditTodoVIewModel by viewModels()
    private lateinit var timePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        val itemId = navArgs.id

        if (itemId == null) {
            editViewModel.createItem(requireContext())
        } else {
            editViewModel.setId(requireContext(), itemId)
        }
        drawContent(editViewModel.getItem())
    }

    private fun drawContent(item: TodoItem): Unit = with(binding) {
        //текст
        etTodo.setText(item.text)

        //качелька
        toggle.isChecked = item.deadline != null

        //кнопка удаления
        if (item.text.isNotEmpty()) {
            ivDelete.setImageResource(R.drawable.delete_red)
            tvDelete.setTextColor(ViewUtils.resolveColorAttr(requireContext(), R.attr.color_red))
        }

        //важность текст
        val text = editViewModel.getPriorityText(requireContext())
        tvPriority.text = text
        if (text == "!! Высокая") {
            tvPriority.setTextColor(ViewUtils.resolveColorAttr(requireContext(), R.attr.color_red))
        } else {
            tvPriority.setTextColor(
                ViewUtils.resolveColorAttr(
                    requireContext(),
                    R.attr.label_tertiary
                )
            )
        }

        //дедлайн
        if (item.deadline != null) {
            tvDate.text = ViewUtils.convertMillisToDateString(item.deadline)
            tvDate.visibility = View.VISIBLE
        }

    }

    private fun registerEvents(): Unit = with(binding) {
        ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        tvPriority.setOnClickListener {
            val popupMenu = PopupMenu(context, tvPriority, 0, 0, R.style.CustomPopupMenu)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            val redElement: MenuItem = popupMenu.menu.getItem(2)
            val s = SpannableString("!! Высокая")
            s.setSpan(
                ForegroundColorSpan(
                    ViewUtils.resolveColorAttr(
                        requireContext(),
                        R.attr.color_red
                    )
                ), 0, s.length, 0
            )
            redElement.title = s
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.middle -> {
                        editViewModel.changePriority(requireContext(), menuItem.title.toString())
                        return@setOnMenuItemClickListener true
                    }

                    R.id.bottom -> {
                        editViewModel.changePriority(requireContext(), menuItem.title.toString())
                        return@setOnMenuItemClickListener true
                    }

                    else -> {
                        editViewModel.changePriority(requireContext(), menuItem.title.toString())
                        return@setOnMenuItemClickListener true
                    }
                }
            }
            popupMenu.show()
        }

        tvSave.setOnClickListener {
            editViewModel.changeText(binding.etTodo.text.toString())
            editViewModel.saveItem()
            findNavController().popBackStack()
        }

        if (!editViewModel.isNewItem) {
            deleteLayout.setOnClickListener {
                editViewModel.deleteItem()
                findNavController().popBackStack()
            }
        }

        val myCalendar = Calendar.getInstance()
        if (editViewModel.getDeadline() != null) {
            myCalendar.timeInMillis = editViewModel.getDeadline()!!
        }

        timePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CalendarStyle,
            { view, year, month, day ->
                tvDate.visibility = View.VISIBLE
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                editViewModel.changeDeadline(myCalendar.timeInMillis)
                binding.tvDate.text =
                    ViewUtils.convertMillisToDateString(editViewModel.getDeadline())
            },
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        timePickerDialog.setOnCancelListener {
            if (binding.tvDate.visibility == View.INVISIBLE) {
                binding.toggle.isChecked = false
            }
        }

        toggle.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                openDatePicker()
            } else {
                tvDate.visibility = View.INVISIBLE
                editViewModel.changeDeadline(null)
            }
        }

        tvDate.setOnClickListener {
            openDatePicker()
        }
    }

    private fun openDatePicker() {
        binding.toggle.isChecked = true
        timePickerDialog.show()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            editViewModel.priorityText.collect { priorityText ->
                if (priorityText == "!! Высокая") {
                    binding.tvPriority.text = priorityText
                    binding.tvPriority.setTextColor(
                        ViewUtils.resolveColorAttr(
                            requireContext(),
                            R.attr.color_red
                        )
                    )
                } else {
                    binding.tvPriority.text = priorityText
                    binding.tvPriority.setTextColor(
                        ViewUtils.resolveColorAttr(
                            requireContext(),
                            R.attr.label_tertiary
                        )
                    )
                }
            }
        }
    }
}