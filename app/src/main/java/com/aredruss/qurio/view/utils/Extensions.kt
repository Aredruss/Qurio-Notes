package com.aredruss.qurio.view.utils

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(directions: NavDirections) =
    safeNavigate(directions.actionId, directions.arguments)

// workaround for https://issuetracker.google.com/issues/118975714
fun NavController.safeNavigate(@IdRes resId: Int, args: Bundle? = null) = try {
    navigate(resId, args)
} catch (e: IllegalArgumentException) {
    Log.e(
        "Navigation error: ",
        "com.crestwavetech.smartskyposservice.safeNavigate failed: $e"
    )

}