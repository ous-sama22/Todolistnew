package com.oussama.todolistapp.tasks

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.oussama.todolistapp.R
import com.oussama.todolistapp.data.AppData
import com.oussama.todolistapp.models.Task
import com.oussama.todolistapp.utils.DateUtils
import com.oussama.todolistapp.viewmodels.TaskViewModel
import java.util.*

class TaskCreationFragment : Fragment() {
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var prioritySpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private val viewModel: TaskViewModel by viewModels()
    private val args: TaskCreationFragmentArgs by navArgs()

    private var startDate: Long = System.currentTimeMillis()
    private var endDate: Long = DateUtils.addDays(System.currentTimeMillis(), 1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupPrioritySpinner()
        setupDatePickers()
        setupButtons()

        args.taskId?.let { taskId ->
            loadTask(taskId)
        }
    }

    private fun initializeViews(view: View) {
        titleEditText = view.findViewById(R.id.titleEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        startDateButton = view.findViewById(R.id.startDateButton)
        endDateButton = view.findViewById(R.id.endDateButton)
        prioritySpinner = view.findViewById(R.id.prioritySpinner)
        saveButton = view.findViewById(R.id.saveButton)
        cancelButton = view.findViewById(R.id.cancelButton)
    }

    private fun setupPrioritySpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priorities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prioritySpinner.adapter = adapter
        }
    }

    private fun setupDatePickers() {
        startDateButton.setOnClickListener { showDatePicker(true) }
        endDateButton.setOnClickListener { showDatePicker(false) }
        updateDateButtons()
    }

    private fun setupButtons() {
        saveButton.setOnClickListener { saveTask() }
        cancelButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val currentDate = if (isStartDate) startDate else endDate
        calendar.timeInMillis = currentDate

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                val selectedDate = calendar.timeInMillis

                if (isStartDate) {
                    if (selectedDate > endDate) {
                        endDate = DateUtils.addDays(selectedDate, 1)
                    }
                    startDate = selectedDate
                } else {
                    if (selectedDate < startDate) {
                        Snackbar.make(
                            requireView(),
                            R.string.error_invalid_dates,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        return@DatePickerDialog
                    }
                    endDate = selectedDate
                }
                updateDateButtons()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            if (!isStartDate) {
                datePicker.minDate = startDate
            }
        }.show()
    }

    private fun updateDateButtons() {
        startDateButton.text = DateUtils.formatDate(startDate)
        endDateButton.text = DateUtils.formatDate(endDate)
    }

    private fun loadTask(taskId: String) {
        viewModel.getTask(taskId)?.let { task ->
            titleEditText.setText(task.title)
            descriptionEditText.setText(task.description)
            startDate = task.startDate
            endDate = task.endDate
            prioritySpinner.setSelection(task.priority.ordinal)
            updateDateButtons()
        }
    }

    private fun saveTask() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val priority = Task.Priority.values()[prioritySpinner.selectedItemPosition]

        if (validateInput(title, description)) {
            val task = if (args.taskId != null) {
                Task(
                    args.taskId!!,
                    title,
                    description,
                    startDate,
                    endDate,
                    priority,
                    false,
                    AppData.currentUser?.id ?: ""
                )
            } else {
                Task(
                    "",
                    title,
                    description,
                    startDate,
                    endDate,
                    priority,
                    false,
                    AppData.currentUser?.id ?: ""
                )
            }

            if (args.taskId != null) {
                viewModel.updateTask(task)
            } else {
                viewModel.addTask(task)
            }

            findNavController().navigateUp()
        }
    }

    private fun validateInput(title: String, description: String): Boolean {
        var isValid = true

        if (title.isEmpty()) {
            titleEditText.error = getString(R.string.error_empty_title)
            isValid = false
        }

        if (description.isEmpty()) {
            descriptionEditText.error = getString(R.string.error_empty_description)
            isValid = false
        }

        if (endDate < startDate) {
            Snackbar.make(
                requireView(),
                R.string.error_invalid_dates,
                Snackbar.LENGTH_SHORT
            ).show()
            isValid = false
        }

        return isValid
    }
}