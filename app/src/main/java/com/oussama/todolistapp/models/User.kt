package com.oussama.todolistapp.models

data class User(
    val id: String = "",
    val email: String,
    val username: String,
    var password: String
) {
    // Secondary constructor for creating user without id (for registration)
    constructor(email: String, username: String, password: String) :
            this("", email, username, password)

    fun toMap(): Map<String, String> {
        return mapOf(
            "id" to id,
            "email" to email,
            "username" to username,
            "password" to password
        )
    }

    companion object {
        fun fromMap(map: Map<String, String>): User {
            return User(
                id = map["id"] ?: "",
                email = map["email"] ?: "",
                username = map["username"] ?: "",
                password = map["password"] ?: ""
            )
        }
    }
}