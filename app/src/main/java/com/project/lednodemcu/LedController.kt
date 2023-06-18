package com.project.lednodemcu

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
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
    private var index:Int

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
        this.index=index
    }

    init {
        view = context.findViewById(id)
        ledTV = view.findViewById(R.id.ledTV)
        ledTV.text="LED ${this.index}"
        ledProgressBar = view.findViewById(R.id.ledProgressBar)
        ledButton = view.findViewById(R.id.ledButton)
        dropDown = view.findViewById(R.id.dropDown)
        ledBrightnessText = view.findViewById(R.id.brightnessText)
        ledBrightnessTV = view.findViewById(R.id.brightnessTV)
        ledSeekBar = view.findViewById(R.id.ledSeekBar)

        ledButton.setOnClickListener {
            toggleLed()
        }
        ledSeekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ledBrightnessTV.text=(progress.toFloat()/255F*100).roundToInt().toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                ledBrightnessText.visibility=View.INVISIBLE
                ledBrightnessTV.visibility=View.VISIBLE

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                ledProgressBar.visibility=View.VISIBLE
                val requestBody = "reset".toRequestBody("plain/text".toMediaType())
                val request = Request.Builder().url(context.url)./*post(requestBody).*/build()
                context.httpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        context.runOnUiThread{ledProgressBar.visibility=View.INVISIBLE}
                        context.connect()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        context.runOnUiThread{
                            ledBrightnessText.visibility=View.VISIBLE
                            ledBrightnessTV.visibility=View.INVISIBLE
                            ledProgressBar.visibility=View.INVISIBLE
                        }
                    }
                })
            }

        })
    }
    private fun toggleLed(){
        ledProgressBar.visibility=View.VISIBLE
        val message=if(ledOn) "${this.index} 0" else "${this.index} 255"
        val requestBody=message.toRequestBody("plain/text".toMediaType())
        val request = Request.Builder().url(context.url)./*post(requestBody).*/build()
        context.httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                context.runOnUiThread{ledProgressBar.visibility=View.INVISIBLE}
                context.connect()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    context.runOnUiThread{
                        ledProgressBar.visibility=View.INVISIBLE
                        if(!ledOn){
                            ledButton.text="TURN OFF"
                            AnimatorUtil.animateBackgroundColor(
                                ledButton,
                                R.color.background,
                                R.color.bluePrimary,
                                this@LedController.context
                            )
                            AnimatorUtil.animateTextColor(
                                ledButton,
                                R.color.bluePrimary,
                                R.color.background,
                                this@LedController.context
                            )
                            dropDown.visibility=View.VISIBLE
                        }
                        else{
                            ledButton.text="TURN ON"
                            AnimatorUtil.animateTextColor(
                                ledButton,
                                R.color.background,
                                R.color.bluePrimary,
                                this@LedController.context
                            )
                            AnimatorUtil.animateBackgroundColor(
                                ledButton,
                                R.color.bluePrimary,
                                R.color.background,
                                this@LedController.context
                            )
                            dropDown.visibility=View.GONE
                        }
                        ledOn=!ledOn
                    }
                }
            }

        })
    }
    fun reset(){
        ledOn=false
        ledSeekBar.progress=255
        ledBrightnessTV.text="100"
        ledButton.background=
            ColorDrawable(ContextCompat.getColor(context, R.color.background))
        ledButton.setTextColor(ContextCompat.getColor(context,R.color.bluePrimary))
        ledButton.text="TURN ON"
        dropDown.visibility=View.GONE
    }

}