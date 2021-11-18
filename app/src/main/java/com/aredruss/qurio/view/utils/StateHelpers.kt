package com.aredruss.qurio.view.utils

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData

class Event<T>(private val content: T) {
    private var consumed = false

    fun peek(): T = content

    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }
}

@MainThread
inline fun <T> MutableLiveData<T>.update(crossinline block: (T) -> T) {
    value?.let { value = block(it) }
}