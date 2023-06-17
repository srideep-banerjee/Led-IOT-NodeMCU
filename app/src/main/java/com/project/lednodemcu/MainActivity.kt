package com.project.lednodemcu

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    private lateinit var msgText: TextView
    private lateinit var buttonText: TextView
    private lateinit var httpClient: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        msgText = findViewById(R.id.buttonText)
        buttonText = findViewById(R.id.buttonText)
        httpClient = OkHttpClient()

    }
}