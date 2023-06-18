package com.project.lednodemcu

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AnimatorUtil {
    companion object {
        fun animateBackgroundColor(
            view: View,
            color1: Int,
            color2: Int,
            context: AppCompatActivity
        ) {
            val colorData = arrayOf(
                ColorDrawable(ContextCompat.getColor(context.applicationContext, color1)),
                ColorDrawable(ContextCompat.getColor(context.applicationContext, color2))
            )
            val trans = TransitionDrawable(colorData)
            view.background = trans
            trans.startTransition(300)
        }

        fun animateTextColor(view: TextView, color1: Int, color2: Int, context: AppCompatActivity) {
            val colorFrom = ContextCompat.getColor(context, color1)
            val colorTo = ContextCompat.getColor(context, color2)
            val colorAnimator = ObjectAnimator.ofObject(
                view, "textColor", ArgbEvaluator(), colorFrom, colorTo
            )
            colorAnimator.duration = 300
            colorAnimator.start()
        }
    }
}