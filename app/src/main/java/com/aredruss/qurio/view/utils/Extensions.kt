package com.aredruss.qurio.view.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.ViewInfoMessageBinding
import com.aredruss.qurio.model.LiteNote
import com.google.android.material.transition.MaterialSharedAxis
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

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

fun DialogFragment.showSingle(fragmentManager: FragmentManager?, tag: String) {
    if (fragmentManager == null) return
    if (fragmentManager.findFragmentByTag(tag) == null) show(fragmentManager, tag)
}

fun Activity.openLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }
    startActivity(
        Intent.createChooser(shareIntent, getString(R.string.open_with))
    )
}

fun Activity.composeEmail() {
    val emailIntent = Intent().apply {
        action = Intent.ACTION_SENDTO
        data = Uri.parse("mailto:aredruss.dev@gmail.com?subject=Qurio")
    }
    startActivity(emailIntent)
}

fun Activity.shareAsText(note: LiteNote) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, "${note.title}\n${note.body}")
    }
    startActivity(intent)
}

fun Activity.saveAsImage(bitmap: Bitmap): Boolean {
    val fileName = "note${Random.nextInt()}.jpg"
    val uri: Uri?
    var stream: OutputStream? = null
    val image: File
    val dir: String

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            stream = contentResolver.openOutputStream(it)
        }
    } else {
        dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/").path
        image = File(dir, fileName)
        stream = FileOutputStream(image)
    }
    return stream?.let { output ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output).also {
            output.flush()
            output.close()
        }
    } ?: false
}

private class ThreadSafeDateFormat(private val pattern: String) : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat = SimpleDateFormat(pattern, Locale.ROOT)
}

private val FORMAT_DATE: ThreadLocal<DateFormat> = ThreadSafeDateFormat("dd/MM/yyyy")

fun Calendar.getClearDate() = this.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.time

fun Date.formatDate() = FORMAT_DATE.get()?.format(this)

fun ViewBinding.getStringArgText(id: Int, value: String): String =
    context().getString(id, value)

fun ViewBinding.getString(id: Int): String = context().getString(id)

fun ViewBinding.context(): Context = this.root.context

fun ViewBinding.getDrawable(resId: Int) = AppCompatResources.getDrawable(context(), resId)

fun ViewInfoMessageBinding.setData(iconId: Int, msgId: Int) {
    iconIv.setImageDrawable(getDrawable(iconId))
    messageTv.text = getString(msgId)
}

fun View.animateVisibility(isVisible: Boolean) {
    this.animate().alpha(if (isVisible) 1f else 0f).apply {
        duration = 250
    }
}
