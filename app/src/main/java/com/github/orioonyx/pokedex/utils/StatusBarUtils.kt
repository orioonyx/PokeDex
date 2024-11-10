package com.github.orioonyx.pokedex.utils

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

object StatusBarUtils {

    fun setStatusBarColor(activity: AppCompatActivity?, color: Int) {
        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = color
        }
    }
}
