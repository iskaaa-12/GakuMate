package com.labactivity.gakumate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.labactivity.gakumate.databinding.ShowAddBinding

class AddCategory: AppCompatActivity() {
    private lateinit var binding: ShowAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        }
    }