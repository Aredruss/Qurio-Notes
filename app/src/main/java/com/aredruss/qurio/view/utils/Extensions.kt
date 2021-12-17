package com.aredruss.qurio.view.utils

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import timber.log.Timber
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.material.transition.MaterialSharedAxis
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun NavController.safeNavigate(directions: NavDirections) =
    safeNavigate(directions.actionId, directions.arguments)

// workaround for https://issuetracker.google.com/issues/118975714
fun NavController.safeNavigate(@IdRes resId: Int, args: Bundle? = null) = try {
    navigate(resId, args)
} catch (e: IllegalArgumentException) {
    Timber.e("safeNavigate failed: $e")
}

fun Fragment.setSlideTransitions() {
    enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
}

private class ThreadSafeDateFormat(private val pattern: String) : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat = SimpleDateFormat(pattern, Locale.ROOT)
}

private val FORMAT_DATE: ThreadLocal<DateFormat> = ThreadSafeDateFormat("dd/MM/yyyy")

private val FORMAT_TIME: ThreadLocal<DateFormat> = ThreadSafeDateFormat("HH:mm:ss")

fun Date.formatDate(): String = FORMAT_DATE.get()!!.format(this)

fun Date.formatTime(): String = FORMAT_TIME.get()!!.format(this)
