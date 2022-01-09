package com.aredruss.qurio.view.utils

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.aredruss.qurio.model.LiteNote
import com.aredruss.qurio.view.notes.ShareDialog
import timber.log.Timber

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSlideTransitions()
    }

    protected fun openShareDialog(note: LiteNote) {
        ShareDialog(
            note = note,
            shareAction = this::shareAsText,
            saveAction = this::saveImageNote
        ).showSingle(
            childFragmentManager,
            ShareDialog.SHARE_DIALOG_TAG
        )
    }

    protected fun saveImageNote(note: Bitmap) {
        try {
            val saved = requireActivity().saveAsImage(note)
            if (saved) {
                Toast.makeText(
                    requireContext(),
                    "Saved the note to camera roll",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                throw Exception("Failed to save note as an image")
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            Timber.e("Error generating image from note: $e")
        }
    }

    protected fun shareAsText(note: LiteNote) = requireActivity().shareAsText(note)
}
