package com.labactivity.gakumate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.labactivity.gakumate.databinding.ActivityAboutAppBinding
import com.labactivity.gakumate.databinding.ActivitySignInBinding

class About_app : AppCompatActivity() {

    private lateinit var binding: ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}