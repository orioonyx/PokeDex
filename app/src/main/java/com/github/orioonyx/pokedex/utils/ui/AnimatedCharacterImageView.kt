package com.github.orioonyx.pokedex.utils.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.github.orioonyx.pokedex.R

class AnimatedCharacterImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var translationYRange: Float = 4f

    private val animator: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "translationY", 0f, translationYRange).apply {
            duration = 1500
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
        }
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.AnimatedCharacterImageView, 0, 0).apply {
            try {
                translationYRange = getFloat(R.styleable.AnimatedCharacterImageView_translationYRange, 4f)
            } finally {
                recycle()
            }
        }
        startAnimation()
    }

    fun startAnimation() {
        if (!animator.isStarted) animator.start()
    }

    fun stopAnimation() {
        if (animator.isStarted) animator.cancel()
    }

    fun pauseAnimation() {
        if (animator.isRunning) animator.pause()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }
}
