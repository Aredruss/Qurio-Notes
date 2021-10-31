package com.aredruss.qurio.helpers

import android.content.Context
import android.content.SharedPreferences

class QurioPrefs(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    companion object {
        private const val prefsName = "QurioPrefs"
        private const val themeKey = "theme"
    }

    var theme: Int
        set(value) = prefs.edit().putInt(themeKey, value).apply()
        get() = prefs.getInt(themeKey, 0)

}