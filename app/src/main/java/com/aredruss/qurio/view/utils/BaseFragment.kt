package com.aredruss.qurio.view.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import timber.log.Timber

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSlideTransitions()
    }

    protected fun shareImageNote(note: Bitmap) {
        try {
            val uri = requireActivity().generateImageFromBitmap(note, "note")
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri.path))
            startActivity(Intent.createChooser(intent, "Share Note"))
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error generating image from note",
                Toast.LENGTH_SHORT
            ).show()
            Timber.e("Error generating image from note: $e")
        }
    }
}
