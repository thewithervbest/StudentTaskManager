package com.example.studenttaskmanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import java.util.Date

class TaskAdapter(
    private val context: Context,
    private val tasks: List<Task>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.task_description)
        val deadlineTextView: TextView = itemView.findViewById(R.id.task_due_date_time)
        val editTaskButton: Button = itemView.findViewById(R.id.editTaskButton)
        val buttonNotification: Button = itemView.findViewById(R.id.buttonNotification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = "${task.id} ${task.title}"
        holder.descriptionTextView.text =
            if (task.desc.trim() != "") task.desc else "No description" // Placeholder text or leave it empty
        holder.deadlineTextView.text = task.dueDateTime

        holder.editTaskButton.setOnClickListener {
            val intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra("task_id", task.id) // Pass task ID
            intent.putExtra("task_title", task.title)
            intent.putExtra("task_description", task.desc)
            intent.putExtra("task_deadline", task.dueDateTime)
            context.startActivity(intent)
        }

        holder.buttonNotification.setOnClickListener {
            scheduleNotification(task)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(task: Task)
    {
        val intent = Intent(context, Notification::class.java)
        val title = task.title
        val message = task.desc
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)

        AlertDialog.Builder(context)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    private fun getTime(): Long
    {
//        val minute = binding.timePicker.minute
//        val hour = binding.timePicker.hour
//        val day = binding.datePicker.dayOfMonth
//        val month = binding.datePicker.month
//        val year = binding.datePicker.year
//
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }
}
