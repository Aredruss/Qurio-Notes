package com.aredruss.qurio.view.notes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.DialogShareBinding
import com.aredruss.qurio.model.LiteNote
import com.aredruss.qurio.view.utils.viewBinding

class ShareDialog(
    private val note: LiteNote,
    private val action: (Bitmap) -> Unit
) : DialogFragment(R.layout.dialog_share) {

    private val binding: DialogShareBinding by viewBinding(DialogShareBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            shareIv.titleTv.text = note.title
            shareIv.bodyTv.text = note.body
            shareIv.dateTv.text = note.date

            shareBtn.setOnClickListener {
                val bitmap = Bitmap.createBitmap(
                    shareIv.root.width,
                    shareIv.root.height,
                    Bitmap.Config.ARGB_8888
                )
                shareIv.root.draw(Canvas(bitmap))
                action(bitmap)
            }
        }
    }

    companion object {
        const val SHARE_DIALOG_TAG = "share"
    }
}