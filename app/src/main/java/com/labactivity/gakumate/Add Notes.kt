package com.labactivity.gakumate

import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.labactivity.gakumate.databinding.ActivityAddNotesBinding
import java.sql.Time
import java.util.*

class AddNotes : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private val tasksList = ArrayList<Tasks>()
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CategoryAdapter(tasksList)

        binding.btnAdd.setOnClickListener {
            val datePicker = binding.datePicker
            val timePicker = binding.timePicker
            val task = binding.checkBoxTasks.text.toString()

            val newDate = getDateFromDatePicker(datePicker)
            val newTime = getTimeFromTimePicker(timePicker)

            val newTask = Tasks(newDate, newTime, task)
            tasksList.add(newTask)
            adapter.notifyDataSetChanged()

            val resultIntent = Intent()
            resultIntent.putExtra("newTask", newTask)
            setResult(RESULT_OK, resultIntent)

            Toast.makeText(applicationContext, "Added Successfully!", Toast.LENGTH_SHORT).show()
            finish()
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
}
