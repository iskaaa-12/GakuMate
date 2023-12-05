package com.labactivity.gakumate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.labactivity.gakumate.databinding.ActivityAddNotesBinding

class AddNotes : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAdd.setOnClickListener(){
            Toast.makeText(applicationContext, "Added Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}