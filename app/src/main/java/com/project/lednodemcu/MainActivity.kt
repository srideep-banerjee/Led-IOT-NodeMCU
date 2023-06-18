package com.project.lednodemcu

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var msgText: TextView
    private lateinit var buttonText: TextView
    lateinit var httpClient: OkHttpClient
    private lateinit var loadingCircle: ProgressBar
    private lateinit var progressedButton: View
    private lateinit var offlineScreen: View
    private lateinit var onlineScreen: View
    private lateinit var ledControllers: Array<LedController>

    val url = "https://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadOfflineScreen()
    }

    fun loadOfflineScreen() {
        buttonText = findViewById(R.id.buttonText)
        msgText = findViewById(R.id.msgText)
        loadingCircle = findViewById(R.id.loadingCircle)
        progressedButton = findViewById(R.id.progressedButton)
        offlineScreen = findViewById(R.id.offlineScreen)
        onlineScreen = findViewById(R.id.onlineScreen)
        httpClient = OkHttpClient()
        progressedButton.setOnClickListener {
            connect()
        }
        connect()
    }

    fun loadOnlineScreen() {
        val ids = arrayOf(R.id.include1, R.id.include2, R.id.include3)
        ledControllers = Array(3) { index -> LedController(this, ids[index], index + 1) }

    }

    fun resetLedUI() {
        runOnUiThread {
            for (led in ledControllers) {
                led.reset()
            }
        }
    }

    fun connect() {
        if (msgText.text.equals("Connecting...")) return
        runOnUiThread {
            offlineScreen.visibility = View.VISIBLE
            onlineScreen.visibility = View.GONE
            msgText.text = "Connecting..."
            buttonText.text = "Connecting..."
            loadingCircle.visibility = View.VISIBLE
            animateBackgroundColor(
                progressedButton,
                R.color.background,
                R.color.bluePrimary,
                this@MainActivity
            )
            animateTextColor(buttonText, R.color.bluePrimary, R.color.background, this@MainActivity)
        }

        val requestBody = "reset".toRequestBody("plain/text".toMediaType())
        val request = Request.Builder().url(url)./*post(requestBody).*/build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    msgText.text = "Device Offline"
                    buttonText.text = "Connect"
                    loadingCircle.visibility = View.INVISIBLE
                    animateBackgroundColor(
                        progressedButton,
                        R.color.bluePrimary, R.color.background, this@MainActivity
                    )
                    animateTextColor(
                        buttonText,
                        R.color.background,
                        R.color.bluePrimary,
                        this@MainActivity
                    )
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        while (!this@MainActivity::ledControllers.isInitialized) ;
                        this@MainActivity.resetLedUI()
                        offlineScreen.visibility = View.GONE
                        onlineScreen.visibility = View.VISIBLE
                    }
                    msgText.text = "Device Offline"
                    buttonText.text = "Connect"
                    loadingCircle.visibility = View.INVISIBLE
                    animateBackgroundColor(
                        progressedButton,
                        R.color.bluePrimary, R.color.background, this@MainActivity
                    )
                    animateTextColor(
                        buttonText,
                        R.color.background,
                        R.color.bluePrimary,
                        this@MainActivity
                    )
                }
            }
        })

        if (!this::ledControllers.isInitialized) {
            loadOnlineScreen()
        }
        resetLedUI()
    }

    fun animateBackgroundColor(
        view: View,
        color1: Int,
        color2: Int,
        context: AppCompatActivity,
        property: String = "backgroundColor"
    ) {
        val colorFrom = ContextCompat.getColor(context, color1)
        val colorTo = ContextCompat.getColor(context, color2)
        val colorAnimator = ObjectAnimator.ofObject(
            view, property, ArgbEvaluator(), colorFrom, colorTo
        )
        colorAnimator.duration = 300
        colorAnimator.start()
    }

    fun animateTextColor(
        view: TextView,
        color1: Int,
        color2: Int,
        context: AppCompatActivity,
        property: String = "textColor"
    ) {
        val colorFrom = ContextCompat.getColor(context, color1)
        val colorTo = ContextCompat.getColor(context, color2)
        val colorAnimator = ObjectAnimator.ofObject(
            view, property, ArgbEvaluator(), colorFrom, colorTo
        )
        colorAnimator.duration = 300
        colorAnimator.start()
    }

}