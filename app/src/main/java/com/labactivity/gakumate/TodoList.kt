package com.labactivity.gakumate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.labactivity.gakumate.databinding.ActivityTodoListBinding
import com.labappdev.todolist.AddNotes

class TodoList : AppCompatActivity() {
    private lateinit var binding: ActivityTodoListBinding
    private val tasksList = ArrayList<Tasks>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.layoutManager = layoutManager

        val adapter = CategoryAdapter(tasksList)
        binding.recyclerViewTasks.adapter = adapter


        binding.floatingAddRecBtn.setOnClickListener {
            val intentAddNotes = Intent(this, AddNotes::class.java)
            startActivityForResult(intentAddNotes, ADD_NOTES_REQUEST)
        }

        binding.imgBtnBack.setOnClickListener(){
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTES_REQUEST && resultCode == RESULT_OK) {
            val newTask = data?.getSerializableExtra("newTask") as? Tasks
            if (newTask != null) {
                tasksList.add(newTask)

                tasksList.sortBy { it.date }

                (binding.recyclerViewTasks.adapter as? CategoryAdapter)?.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val ADD_NOTES_REQUEST = 1
    }
}