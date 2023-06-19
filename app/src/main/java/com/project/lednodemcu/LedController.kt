package com.project.lednodemcu

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import kotlin.math.roundToInt

class LedController(context: MainActivity, id: Int, index: Int) {
    private var context: MainActivity
    private var id = 0
    private var index: Int

    private var view: View
    private var ledTV: TextView
    private var ledProgressBar: View
    private var ledButton: MaterialButton
    private var dropDown: View
    private var ledBrightnessText: View
    private var ledBrightnessTV: TextView
    private var ledSeekBar: SeekBar

    private var ledOn = false

    init {
        this.context = context
        this.id = id
        this.index = index
    }

    init {
        view = context.findViewById(id)
        ledTV = view.findViewById(R.id.ledTV)
        ledTV.text = "LED ${this.index}"
        ledProgressBar = view.findViewById(R.id.ledProgressBar)
        ledButton = view.findViewById(R.id.ledButton)
        dropDown = view.findViewById(R.id.dropDown)
        ledBrightnessText = view.findViewById(R.id.brightnessText)
        ledBrightnessTV = view.findViewById(R.id.brightnessTV)
        ledSeekBar = view.findViewById(R.id.ledSeekBar)

        ledButton.setOnClickListener {
            toggleLed()
        }
        ledSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ledBrightnessTV.text = (progress.toFloat() / 255F * 100).roundToInt().toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                ledBrightnessText.visibility = View.INVISIBLE
                ledBrightnessTV.visibility = View.VISIBLE

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                ledProgressBar.visibility = View.VISIBLE
                val requestBody = "$index ${ledSeekBar.progress}".toRequestBody("plain/text".toMediaType())
                val request = Request.Builder().url(context.url).post(requestBody).build()
                context.httpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        context.runOnUiThread {
                            ledBrightnessText.visibility = View.VISIBLE
                            ledBrightnessTV.visibility = View.INVISIBLE
                            ledProgressBar.visibility = View.INVISIBLE
                        }
                        context.connect()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        context.runOnUiThread {
                            ledBrightnessText.visibility = View.VISIBLE
                            ledBrightnessTV.visibility = View.INVISIBLE
                            ledProgressBar.visibility = View.INVISIBLE
                        }
                    }
                })
            }

        })
    }

    private fun toggleLed() {
        ledProgressBar.visibility = View.VISIBLE
        val message = if (ledOn) "${this.index} 0" else "${this.index} ${ledSeekBar.progress}"
        val requestBody = message.toRequestBody("plain/text".toMediaType())
        val request = Request.Builder().url(context.url).post(requestBody).build()
        context.httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                context.runOnUiThread { ledProgressBar.visibility = View.INVISIBLE }
                context.connect()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    context.runOnUiThread {
                        ledProgressBar.visibility = View.INVISIBLE
                        if (!ledOn) {
                            ledButton.text = "TURN OFF"
                            animateBackgroundColor(
                                ledButton,
                                R.color.background,
                                R.color.bluePrimary,
                                this@LedController.context,

                                )
                            animateTextColor(
                                ledButton,
                                R.color.bluePrimary,
                                R.color.background,
                                this@LedController.context
                            )
                            dropDown.visibility = View.VISIBLE
                        } else {
                            ledButton.text = "TURN ON"
                            animateTextColor(
                                ledButton,
                                R.color.background,
                                R.color.bluePrimary,
                                this@LedController.context
                            )
                            animateBackgroundColor(
                                ledButton,
                                R.color.bluePrimary,
                                R.color.background,
                                this@LedController.context
                            )
                            dropDown.visibility = View.GONE
                        }
                        ledOn = !ledOn
                    }
                }
            }

        })
    }

    fun reset() {
        ledOn = false
        ledSeekBar.progress = 255
        ledBrightnessTV.text = "100"
        val temp = ledButton.background
        temp.setTint(ContextCompat.getColor(context, R.color.background))
        ledButton.setBackgroundDrawable(temp)
        ledButton.setTextColor(ContextCompat.getColor(context, R.color.bluePrimary))
        ledButton.text = "TURN ON"
        dropDown.visibility = View.GONE
    }

    fun animateBackgroundColor(
        view: MaterialButton,
        color1: Int,
        color2: Int,
        context: AppCompatActivity,
        property: String = "tint"
    ) {
        val colorFrom = ContextCompat.getColor(context, color1)
        val colorTo = ContextCompat.getColor(context, color2)
        val colorAnimator = ObjectAnimator.ofObject(
            view.background, property, ArgbEvaluator(), colorFrom, colorTo
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