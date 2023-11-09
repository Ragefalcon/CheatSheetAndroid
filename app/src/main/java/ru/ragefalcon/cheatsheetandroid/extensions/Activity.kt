package ru.ragefalcon.cheatsheetandroid.extensions

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.Display
import androidx.core.hardware.display.DisplayManagerCompat

fun Activity.screenValue(): Size {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val defaultDisplay =
            DisplayManagerCompat.getInstance(this).getDisplay(Display.DEFAULT_DISPLAY)
        val displayContext = createDisplayContext(defaultDisplay!!)

        val width = displayContext.resources.displayMetrics.widthPixels
        val height = displayContext.resources.displayMetrics.heightPixels

        return Size(width, height)
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        return Size(width, height)
    }
}
