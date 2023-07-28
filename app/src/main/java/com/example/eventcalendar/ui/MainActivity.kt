package com.example.eventcalendar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}