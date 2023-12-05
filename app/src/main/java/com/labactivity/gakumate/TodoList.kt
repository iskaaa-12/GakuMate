package com.labactivity.gakumate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.labactivity.gakumate.databinding.ActivityTodoListBinding

class TodoList : AppCompatActivity() {
    private lateinit var binding: ActivityTodoListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingAddRecBtn.setOnClickListener(){
            val intentAddNotes = Intent(this, AddNotes::class.java)
            startActivity(intentAddNotes)
        }
    }
}