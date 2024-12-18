package com.oussama.todolistapp.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oussama.todolistapp.R
import com.oussama.todolistapp.adapters.TaskAdapter
import com.oussama.todolistapp.models.Task
import com.oussama.todolistapp.utils.PreferenceManager
import com.oussama.todolistapp.viewmodels.TaskViewModel

class TaskListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateTextView: TextView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var preferenceManager: PreferenceManager
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())
        setupViews(view)
        setupRecyclerView()
        observeTasks()
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.taskRecyclerView)
        emptyStateTextView = view.findViewById(R.id.emptyStateTextView)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)

        taskAdapter = TaskAdapter(
            mutableListOf(),
            onTaskCheckedListener = { task -> onTaskStatusChanged(task) },
            onTaskClickListener = { task -> onTaskClicked(task) },
            onEditClickListener = { task -> onEditTask(task) },
            onDeleteClickListener = { task -> onDeleteTask(task) }
        )

        recyclerView.adapter = taskAdapter
    }

    private fun observeTasks() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateTasks(tasks)
            updateEmptyState(tasks.isEmpty())
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        emptyStateTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun onTaskStatusChanged(task: Task) {
        viewModel.updateTask(task)
    }

    private fun onTaskClicked(task: Task) {
        navigateToTaskEdit(task.id)
    }

    private fun onEditTask(task: Task) {
        navigateToTaskEdit(task.id)
    }

    private fun onDeleteTask(task: Task) {
        viewModel.deleteTask(task.id)
    }

    private fun navigateToTaskEdit(taskId: String) {
        val action = TaskListFragmentDirections
            .actionTaskListFragmentToTaskCreationFragment(
                taskId = taskId,
                title = getString(R.string.edit_task)
            )
        findNavController().navigate(action)
    }

    fun navigateToNewTask() {
        val action = TaskListFragmentDirections
            .actionTaskListFragmentToTaskCreationFragment(
                taskId = null,
                title = getString(R.string.new_task)
            )
        findNavController().navigate(action)
    }
}