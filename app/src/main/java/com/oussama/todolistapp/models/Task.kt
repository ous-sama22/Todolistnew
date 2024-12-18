package com.oussama.todolistapp.models


data class Task(
    val id: String = "",
    var title: String,
    var description: String,
    var startDate: Long,
    var endDate: Long,
    var priority: Priority = Priority.MEDIUM,
    var isCompleted: Boolean = false,
    val userId: String
) {
    enum class Priority {
        LOW, MEDIUM, HIGH
    }

    // Secondary constructor for creating task without id
    constructor(
        title: String,
        description: String,
        startDate: Long,
        endDate: Long,
        priority: Priority,
        userId: String
    ) : this("", title, description, startDate, endDate, priority, false, userId)

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "startDate" to startDate,
            "endDate" to endDate,
            "priority" to priority.name,
            "isCompleted" to isCompleted,
            "userId" to userId
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Task {
            return Task(
                id = map["id"] as String,
                title = map["title"] as String,
                description = map["description"] as String,
                startDate = map["startDate"] as Long,
                endDate = map["endDate"] as Long,
                priority = Priority.valueOf(map["priority"] as String),
                isCompleted = map["isCompleted"] as Boolean,
                userId = map["userId"] as String
            )
        }
    }
}