package com.oussama.todolistapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oussama.todolistapp.data.AppData
import com.oussama.todolistapp.models.Task
import com.oussama.todolistapp.utils.Constants

class TaskViewModel : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private var currentSortOrder = Constants.DEFAULT_SORT_ORDER
    private var currentFilterType = Constants.DEFAULT_FILTER_TYPE

    init {
        updateTasks()
    }

    fun addTask(task: Task) {
        AppData.addTask(task)
        updateTasks()
    }

    fun updateTask(task: Task) {
        AppData.updateTask(task)
        updateTasks()
    }

    fun deleteTask(taskId: String) {
        AppData.deleteTask(taskId)
        updateTasks()
    }

    fun getTask(taskId: String): Task? {
        return AppData.tasks.find { it.id == taskId }
    }

    fun setSortOrder(sortOrder: String) {
        currentSortOrder = sortOrder
        updateTasks()
    }

    fun setFilterType(filterType: String) {
        currentFilterType = filterType
        updateTasks()
    }

    private fun updateTasks() {
        var filteredTasks = when (currentFilterType) {
            Constants.FILTER_COMPLETED -> AppData.getCurrentUserTasks().filter { it.isCompleted }
            Constants.FILTER_PENDING -> AppData.getCurrentUserTasks().filter { !it.isCompleted }
            else -> AppData.getCurrentUserTasks()
        }

        filteredTasks = when (currentSortOrder) {
            Constants.SORT_BY_DATE -> filteredTasks.sortedBy { it.startDate }
            Constants.SORT_BY_PRIORITY -> filteredTasks.sortedByDescending { it.priority }
            else -> filteredTasks
        }

        _tasks.value = filteredTasks
    }
}