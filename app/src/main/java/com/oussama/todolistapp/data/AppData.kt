package com.oussama.todolistapp.data

import com.oussama.todolistapp.models.Task
import com.oussama.todolistapp.models.User

object AppData {
    // Store currently logged in user
    var currentUser: User? = null

    // Arrays to store data
    val users = mutableListOf(
        User("1", "test@test.com", "Test User", "password"),
        User("2", "user@example.com", "Demo User", "123456")
    )

    val tasks = mutableListOf(
        Task(
            "1",
            "Complete Android Project",
            "Finish the todo list application",
            System.currentTimeMillis(),
            System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // 7 days from now
            Task.Priority.HIGH,
            false,
            "1"
        ),
        Task(
            "2",
            "Buy Groceries",
            "Get milk, eggs, and bread",
            System.currentTimeMillis(),
            System.currentTimeMillis() + (24 * 60 * 60 * 1000), // 1 day from now
            Task.Priority.MEDIUM,
            false,
            "1"
        ),
        Task(
            "3",
            "Call Mom",
            "Weekly check-in call",
            System.currentTimeMillis(),
            System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000), // 3 days from now
            Task.Priority.LOW,
            false,
            "2"
        )
    )

    // Simple functions to manage data
    fun addTask(task: Task) {
        val newId = (tasks.maxOfOrNull { it.id.toInt() } ?: 0) + 1
        tasks.add(task.copy(id = newId.toString()))
    }

    fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    fun deleteTask(taskId: String) {
        tasks.removeAll { it.id == taskId }
    }

    fun addUser(email: String, username: String, password: String) {
        val newId = (users.maxOfOrNull { it.id.toInt() } ?: 0) + 1
        users.add(User(newId.toString(), email, username, password))
    }

    fun loginUser(email: String, password: String): Boolean {
        currentUser = users.find { it.email == email && it.password == password }
        return currentUser != null
    }

    fun logout() {
        currentUser = null
    }

    fun getCurrentUserTasks(): List<Task> {
        return tasks.filter { it.userId == currentUser?.id }
    }
}