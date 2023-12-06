package com.labactivity.gakumate

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.labactivity.gakumate.databinding.ActivityAddNotesBinding
import java.sql.Time
import java.util.*

class AddNotes : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var todoList: LinearLayout
    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var adapter: TaskAdapter
    private val tasksList = ArrayList<Tasks>()

    // SharedPreferences key and file name
    private val PREFS_NAME = "MyPrefsFile"
    private val TASKS_KEY = "tasks_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using data binding
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views after inflating the layout
        editText = binding.edtTxtAddTasks
        todoList = binding.todoList

        // Initialize SharedPreferences
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTasks: String? = prefs.getString(TASKS_KEY, null)

        // If there are saved tasks, deserialize them and add to the tasksList
        savedTasks?.let {
            val savedTaskList: ArrayList<Tasks> = deserializeTaskList(it)
            tasksList.addAll(savedTaskList)
        }

        // Initialize your adapter with the tasksList
        adapter = TaskAdapter(this, tasksList)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                updateTodoList()
            }
        })

        binding.btnAdd.setOnClickListener {
            val datePicker = binding.datePicker
            val timePicker = binding.timePicker

            val newDate = getDateFromDatePicker(datePicker)
            val newTime = getTimeFromTimePicker(timePicker)
            val newTaskText = binding.edtTxtAddTasks.text.toString()

            val newTask = Tasks(newDate, newTime, newTaskText)
            tasksList.add(newTask)
            adapter.notifyDataSetChanged()

            // Save the updated tasksList to SharedPreferences
            saveTasksToSharedPreferences()

            val resultIntent = Intent()
            resultIntent.putExtra("newTask", newTask)
            setResult(RESULT_OK, resultIntent)

            Toast.makeText(applicationContext, "Added Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        // Initialize SharedPreferences
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTasks: String? = prefs.getString(TASKS_KEY, null)

        // If there are saved tasks, deserialize them and add to the tasksList
        savedTasks?.let {
            val savedTaskList: ArrayList<Tasks> = deserializeTaskList(it)
            tasksList.clear() // Clear existing tasks
            tasksList.addAll(savedTaskList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun updateTodoList() {
        // Clear existing items
        todoList.removeAllViews()

        // Split text into lines
        val lines = editText.text.toString().split("\n")

        // Add a checkbox for each line
        for (line in lines) {
            if (!line.trim().isEmpty()) {
                val checkbox = CheckBox(this)
                checkbox.text = line

                todoList.addView(checkbox)

                // Add text to txtViewNotes (which is actually todoList)
                val textView = TextView(this)
                textView.text = line
                todoList.addView(textView)
            }
        }
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }

    private fun getTimeFromTimePicker(timePicker: TimePicker): Time {
        val hour = timePicker.hour
        val minute = timePicker.minute

        val calendar = Calendar.getInstance()
        calendar.set(0, 0, 0, hour, minute)

        return Time(calendar.timeInMillis)
    }

    private fun saveTasksToSharedPreferences() {
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        // Serialize the tasksList and save it to SharedPreferences
        editor.putString(TASKS_KEY, serializeTaskList(tasksList))
        editor.apply()
    }

    private fun serializeTaskList(taskList: ArrayList<Tasks>): String {
        // Implement your serialization logic here (e.g., using Gson library)
        // For simplicity, I'll use a basic approach
        val serializedList = StringBuilder()

        for (task in taskList) {
            serializedList.append("${task.date.time},${task.time.time},${task.taskText}|")
        }

        return serializedList.toString()
    }

    private fun deserializeTaskList(serializedData: String): ArrayList<Tasks> {
        // Implement your deserialization logic here (e.g., using Gson library)
        // For simplicity, I'll use a basic approach
        val taskList = ArrayList<Tasks>()

        val taskArray = serializedData.split("|")

        for (taskData in taskArray) {
            if (taskData.isNotEmpty()) {
                val taskFields = taskData.split(",")
                val date = Date(taskFields[0].toLong())
                val time = Time(taskFields[1].toLong())
                val taskText = taskFields[2]

                val task = Tasks(date, time, taskText)
                taskList.add(task)
            }
        }

        return taskList
    }
}
