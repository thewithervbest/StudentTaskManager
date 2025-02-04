package com.example.studenttaskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Date

class CreateTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val taskTitle: EditText = findViewById(R.id.task_title)
        val taskDesc: EditText = findViewById(R.id.task_desc)
        val taskDate: DatePicker = findViewById(R.id.task_date)
        val taskTime: TimePicker = findViewById(R.id.task_time)
        val button: Button = findViewById(R.id.button_create_task)

        button.setOnClickListener {
            val title = taskTitle.text.toString().trim()
            val desc = taskDesc.text.toString().trim()
            // Get date from DatePicker
            val year = taskDate.year
            val month = taskDate.month + 1 // Month is 0-based
            val day = taskDate.dayOfMonth

            // Get time from TimePicker
            val hour = taskTime.hour
            val minute = taskTime.minute

            // Format date and time
            val dateTime = String.format("%04d-%02d-%02d %02d:%02d", year, month, day, hour, minute)



            if (title == "")
                Toast.makeText(this, "Title can't be empty", Toast.LENGTH_LONG).show()
            else {
                val task = Task(id=null, title=title, desc=desc, dueDateTime=dateTime)

                val db = DbHelper(this, null)
                db.addTask(task)
                Toast.makeText(this, "Task $title created", Toast.LENGTH_LONG).show()
                taskTitle.text.clear()
                taskDesc.text.clear()
            }
        }


    }
}