package com.project.lednodemcu

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView

class AnimatorUtil {
    companion object{
        fun animateBackgroundColor(view: View, color1: Int, color2: Int){
            val colorAnimator = ObjectAnimator.ofObject(
                view, "backgroundColor", ArgbEvaluator(), color1, color2
            )
            colorAnimator.duration = 300
            colorAnimator.start()
        }
        fun animateTextColor(view: TextView, color1: Int, color2: Int){
            val colorAnimator = ObjectAnimator.ofObject(
                view, "textColor", ArgbEvaluator(), color1, color2
            )
            colorAnimator.duration = 300
            colorAnimator.start()
        }
    }
}