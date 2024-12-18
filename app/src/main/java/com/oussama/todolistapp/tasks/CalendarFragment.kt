package com.oussama.todolistapp.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.oussama.todolistapp.R
import com.oussama.todolistapp.adapters.TaskAdapter
import com.oussama.todolistapp.models.Task
import com.oussama.todolistapp.utils.DateUtils
import com.oussama.todolistapp.viewmodels.TaskViewModel

class CalendarFragment : Fragment() {
    private lateinit var calendarView: CalendarView
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var emptyStateTextView: TextView
    private lateinit var taskAdapter: TaskAdapter
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupCalendar()
        setupRecyclerView()
        setupObservers()
        setupItemTouchHelper()
    }

    private fun setupViews(view: View) {
        calendarView = view.findViewById(R.id.calendarView)
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView)
        emptyStateTextView = view.findViewById(R.id.emptyStateTextView)
    }

    private fun setupCalendar() {
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = DateUtils.formatDateForDisplay(year, month, dayOfMonth)
            filterTasksByDate(selectedDate)
        }
    }

    private fun setupRecyclerView() {
        taskRecyclerView.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter(
            mutableListOf(),
            onTaskCheckedListener = { task -> updateTask(task) },
            onTaskClickListener = { task -> navigateToTaskEdit(task) },
            onEditClickListener = { task -> navigateToTaskEdit(task) },
            onDeleteClickListener = { task -> deleteTask(task) }
        )
        taskRecyclerView.adapter = taskAdapter
    }

    private fun setupObservers() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            val selectedDate = calendarView.date
            val filteredTasks = tasks.filter { task ->
                DateUtils.isSameDay(task.startDate, selectedDate)
            }
            updateTaskList(filteredTasks)
        }
    }

    private fun setupItemTouchHelper() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = taskAdapter.getTaskAt(position)

                when (direction) {
                    ItemTouchHelper.LEFT -> deleteTask(task)
                    ItemTouchHelper.RIGHT -> navigateToTaskEdit(task)
                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(taskRecyclerView)
    }

    private fun updateTaskList(tasks: List<Task>) {
        taskAdapter.updateTasks(tasks)
        updateEmptyState(tasks.isEmpty())
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        emptyStateTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        taskRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun filterTasksByDate(selectedDate: String) {
        viewModel.tasks.value?.let { tasks ->
            val selectedTimestamp = DateUtils.parseDate(selectedDate)
            val filteredTasks = tasks.filter { task ->
                DateUtils.isSameDay(task.startDate, selectedTimestamp)
            }
            updateTaskList(filteredTasks)
        }
    }

    private fun updateTask(task: Task) {
        viewModel.updateTask(task)
    }

    private fun deleteTask(task: Task) {
        viewModel.deleteTask(task.id)
        Snackbar.make(
            requireView(),
            "Task deleted",
            Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            viewModel.addTask(task)
        }.show()
    }

    private fun navigateToTaskEdit(task: Task) {
        val action = CalendarFragmentDirections
            .actionCalendarFragmentToTaskCreationFragment(
                taskId = task.id,
                title = getString(R.string.edit_task)
            )
        findNavController().navigate(action)
    }
}