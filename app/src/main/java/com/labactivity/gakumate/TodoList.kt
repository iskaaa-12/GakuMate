package com.labactivity.gakumate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.labactivity.gakumate.databinding.ActivityTodoListBinding

class TodoList : AppCompatActivity() {
    private lateinit var binding: ActivityTodoListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}