package com.oussama.todolistapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oussama.todolistapp.R
import com.oussama.todolistapp.models.Task
import com.oussama.todolistapp.utils.DateUtils

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onTaskCheckedListener: (Task) -> Unit,
    private val onTaskClickListener: (Task) -> Unit,
    private val onEditClickListener: (Task) -> Unit,
    private val onDeleteClickListener: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        private val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        private val priorityIndicator: View = itemView.findViewById(R.id.priorityIndicator)

        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            dateTextView.text = "${DateUtils.formatDate(task.startDate)} - ${DateUtils.formatDate(task.endDate)}"
            checkBox.isChecked = task.isCompleted

            val colorRes = when (task.priority) {
                Task.Priority.HIGH -> R.color.priority_high
                Task.Priority.MEDIUM -> R.color.priority_medium
                Task.Priority.LOW -> R.color.priority_low
            }
            priorityIndicator.setBackgroundResource(colorRes)

            setupListeners(task)
        }

        private fun setupListeners(task: Task) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (task.isCompleted != isChecked) {
                    task.isCompleted = isChecked
                    onTaskCheckedListener(task)
                }
            }

            itemView.setOnClickListener { onTaskClickListener(task) }
            editButton.setOnClickListener { onEditClickListener(task) }
            deleteButton.setOnClickListener { onDeleteClickListener(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    fun getTaskAt(position: Int): Task = tasks[position]
}