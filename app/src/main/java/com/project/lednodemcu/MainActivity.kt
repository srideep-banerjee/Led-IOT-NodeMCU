package com.project.lednodemcu

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.project.lednodemcu.AnimatorUtil.Companion.animateBackgroundColor
import com.project.lednodemcu.AnimatorUtil.Companion.animateTextColor
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var msgText: TextView
    private lateinit var buttonText: TextView
    private lateinit var httpClient: OkHttpClient
    private lateinit var loadingCircle: ProgressBar
    private lateinit var progressedButton: MaterialCardView

    private val url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonText = findViewById(R.id.buttonText)
        msgText = findViewById(R.id.msgText)
        loadingCircle=findViewById(R.id.loadingCircle)
        progressedButton=findViewById(R.id.progressedButton)
        httpClient = OkHttpClient()

    }

    fun connect() {
        msgText.text="Connecting..."
        buttonText.text="Connecting..."
        loadingCircle.visibility=View.VISIBLE
        animateBackgroundColor(progressedButton,R.color.background,R.color.bluePrimary)
        animateTextColor(buttonText,R.color.bluePrimary,R.color.background)

        val requestBody = "hello".toRequestBody("plain/text".toMediaType())
        val request = Request.Builder().url(url).post(requestBody).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    msgText.text="Device Offline"
                    buttonText.text="Connect"
                    loadingCircle.visibility=View.INVISIBLE
                    animateBackgroundColor(progressedButton,R.color.bluePrimary,R.color.background)
                    animateTextColor(buttonText,R.color.background,R.color.bluePrimary)
                }
            }

            override fun onResponse(call: Call, response: Response) {

            }
        })
    }

}