package com.aredruss.qurio.view.utils

import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSlideTransitions()
    }

    protected fun shareImageNote(note: Bitmap) {
        requireActivity().generateImageFromBitmap(note, "note")
    }
}