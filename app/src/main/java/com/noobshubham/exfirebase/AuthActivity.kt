package com.noobshubham.exfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.noobshubham.exfirebase.databinding.ActivityAuthBinding
import com.noobshubham.exfirebase.databinding.ActivityMainBinding

class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}