package com.oussama.todolistapp.utils

import java.io.File
import kotlin.text.StringBuilder

fun main() {
    val currentDir = System.getProperty("user.dir")
    println("Current directory: $currentDir")

    object {
        private val projectRoot = File(currentDir)
        private val BASE_PACKAGE = "com.oussama.todolistapp"

        fun generateProjectStructure() {
            // Create main directories
            val directories = listOf(
                "app/src/main/java/com/oussama/todolistapp/activities",
                "app/src/main/java/com/oussama/todolistapp/auth",
                "app/src/main/java/com/oussama/todolistapp/tasks",
                "app/src/main/java/com/oussama/todolistapp/adapters",
                "app/src/main/java/com/oussama/todolistapp/models",
                "app/src/main/java/com/oussama/todolistapp/utils",
                "app/src/main/java/com/oussama/todolistapp/viewmodels",
                "app/src/main/res/layout"
            )

            directories.forEach { dir ->
                File(projectRoot, dir).mkdirs()
            }

            // Activities
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/activities/MainActivity.kt",
                "MainActivity", "activities", "AppCompatActivity",
                listOf("androidx.appcompat.app.AppCompatActivity")
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/activities/AuthActivity.kt",
                "AuthActivity", "activities", "AppCompatActivity",
                listOf("androidx.appcompat.app.AppCompatActivity")
            )

            // Auth Fragments
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/auth/LoginFragment.kt",
                "LoginFragment", "auth", "Fragment",
                listOf("androidx.fragment.app.Fragment")
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/auth/SignupFragment.kt",
                "SignupFragment", "auth", "Fragment",
                listOf("androidx.fragment.app.Fragment")
            )

            // Task Fragments
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/tasks/TaskListFragment.kt",
                "TaskListFragment", "tasks", "Fragment",
                listOf("androidx.fragment.app.Fragment")
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/tasks/TaskCreationFragment.kt",
                "TaskCreationFragment", "tasks", "Fragment",
                listOf("androidx.fragment.app.Fragment")
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/tasks/CalendarFragment.kt",
                "CalendarFragment", "tasks", "Fragment",
                listOf("androidx.fragment.app.Fragment")
            )

            // Models
            createTaskModel()
            createUserModel()

            // Utils
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/utils/Constants.kt",
                "Constants", "utils"
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/utils/DateUtils.kt",
                "DateUtils", "utils"
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/utils/PreferenceManager.kt",
                "PreferenceManager", "utils"
            )

            // ViewModels
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/viewmodels/TaskViewModel.kt",
                "TaskViewModel", "viewmodels", "ViewModel",
                listOf("androidx.lifecycle.ViewModel")
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/viewmodels/AuthViewModel.kt",
                "AuthViewModel", "viewmodels", "ViewModel",
                listOf("androidx.lifecycle.ViewModel")
            )

            // Adapters
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/adapters/TaskAdapter.kt",
                "TaskAdapter", "adapters", "RecyclerView.Adapter<TaskAdapter.TaskViewHolder>",
                listOf("androidx.recyclerview.widget.RecyclerView")
            )
            createKotlinFile(
                "app/src/main/java/com/oussama/todolistapp/adapters/AuthPagerAdapter.kt",
                "AuthPagerAdapter", "adapters", "FragmentStateAdapter",
                listOf("androidx.viewpager2.adapter.FragmentStateAdapter")
            )

            // Layout files
            val layoutFiles = listOf(
                "activity_main",
                "activity_auth",
                "fragment_login",
                "fragment_signup",
                "fragment_task_list",
                "fragment_task_creation",
                "fragment_calendar",
                "item_task"
            )

            layoutFiles.forEach { layoutName ->
                createLayoutFile(layoutName)
            }

            println("Project structure generated successfully!")
        }

        private fun createKotlinFile(
            filePath: String,
            className: String,
            packageSuffix: String,
            parentClass: String = "",
            imports: List<String> = listOf()
        ) {
            val file = File(projectRoot, filePath)
            file.parentFile.mkdirs()

            val content = StringBuilder().apply {
                append("package $BASE_PACKAGE.$packageSuffix\n\n")
                imports.forEach { append("import $it\n") }
                if (imports.isNotEmpty()) append("\n")
                append("class $className")
                if (parentClass.isNotEmpty()) append(" : $parentClass")
                append(" {\n")
                append("    // TODO: Implement $className\n")
                append("}\n")
            }.toString()

            file.writeText(content)
            println("Created file: $filePath")
        }

        private fun createTaskModel() {
            val file = File(projectRoot, "app/src/main/java/com/oussama/todolistapp/models/Task.kt")
            file.parentFile.mkdirs()

            val content = """
                package $BASE_PACKAGE.models

                import java.util.Date
                import java.util.UUID

                data class Task(
                    val id: String = UUID.randomUUID().toString(),
                    var title: String = "",
                    var description: String = "",
                    var dueDate: Date? = null,
                    var priority: Priority = Priority.MEDIUM,
                    var isCompleted: Boolean = false
                )

                enum class Priority {
                    LOW, MEDIUM, HIGH
                }
            """.trimIndent()

            file.writeText(content)
            println("Created Task model")
        }

        private fun createUserModel() {
            val file = File(projectRoot, "app/src/main/java/com/oussama/todolistapp/models/User.kt")
            file.parentFile.mkdirs()

            val content = """
                package $BASE_PACKAGE.models

                import java.util.UUID

                data class User(
                    val id: String = UUID.randomUUID().toString(),
                    val name: String = "",
                    val email: String = ""
                )
            """.trimIndent()

            file.writeText(content)
            println("Created User model")
        }

        private fun createLayoutFile(name: String) {
            val file = File(projectRoot, "app/src/main/res/layout/$name.xml")
            file.parentFile.mkdirs()

            val content = """
                <?xml version="1.0" encoding="utf-8"?>
                <androidx.constraintlayout.widget.ConstraintLayout 
                    xmlns:android="http://schemas.android.com/apk/res/android"
                    xmlns:app="http://schemas.android.com/apk/res-auto"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent">

                </androidx.constraintlayout.widget.ConstraintLayout>
            """.trimIndent()

            file.writeText(content)
            println("Created layout file: $name.xml")
        }
    }.generateProjectStructure()
}