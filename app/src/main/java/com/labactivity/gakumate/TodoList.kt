package com.labactivity.gakumate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.labactivity.gakumate.databinding.ActivityTodoListBinding

class TodoList : AppCompatActivity() {

    private lateinit var binding: ActivityTodoListBinding
    private lateinit var tasksSharedPreferencesManager: TasksSharedPreferencesManager
    private val tasksList = ArrayList<Tasks>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra("categoryName")

        if (!categoryName.isNullOrBlank()) {
            binding.txtViewCategoryTitle.text = categoryName
            tasksSharedPreferencesManager = TasksSharedPreferencesManager(this, categoryName)
        } else {
            // Handle the case when categoryName is null or blank
            finish()
            return
        }

        tasksList.addAll(tasksSharedPreferencesManager.getTasks())

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.layoutManager = layoutManager

        adapter = TaskAdapter(this, tasksList, tasksSharedPreferencesManager)
        binding.recyclerViewTasks.adapter = adapter

        binding.floatingAddRecBtn.setOnClickListener {
            val intentAddNotes = Intent(this, AddNotes::class.java)
            startActivityForResult(intentAddNotes, ADD_NOTES_REQUEST)
        }

        binding.imgBtnBack.setOnClickListener {
            finish()
        }
    }

    private fun initializeTasksManager(categoryName: String) {
        tasksSharedPreferencesManager = TasksSharedPreferencesManager(this, categoryName)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTES_REQUEST && resultCode == RESULT_OK) {
            val newTask = data?.getSerializableExtra("newTask") as? Tasks
            if (newTask != null) {
                tasksList.add(newTask)
                tasksList.sortBy { it.date }

                adapter.notifyDataSetChanged()

                // Save the updated tasks list to SharedPreferences
                tasksSharedPreferencesManager.saveTasks(tasksList)
            }
        }
    }



    companion object {
        const val ADD_NOTES_REQUEST = 1
    }
}