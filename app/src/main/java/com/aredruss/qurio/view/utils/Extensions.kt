package com.aredruss.qurio.view.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.aredruss.qurio.R
import com.google.android.material.transition.MaterialSharedAxis
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun EditText.setFocusChangeListener(action: () -> Unit) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) action()
    }
}

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

fun Activity.shareLink(url: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    startActivity(
        Intent.createChooser(shareIntent, getString(R.string.share_to))
    )
}

//fun Activity.generateImageFromBitmap(bitmap: Bitmap, title: String): Uri {
//    //create a file to write bitmap data
//    return try {
//
//        Uri.fromFile(file)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        throw Exception("Failed to create File")// it will return null
//    }
//}

fun Activity.bitmapToUriConverter(mBitmap: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png")
        val stream = FileOutputStream(file)
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        uri = Uri.fromFile(file)
    } catch (e: IOException) {
        Timber.e("IOException while trying to write file for sharing: %s", e.message)
    }
    Timber.e(uri.toString())
    return uri
}

fun Activity.convertBitmapToUri(bitmap: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        val fileName = "note.png"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.IS_PENDING, 1)
        } else {
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val file = File(directory, fileName)
            Timber.e(file.absolutePath)
            values.put(MediaStore.MediaColumns.DATA, file.absolutePath)
        }
        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            contentResolver.openOutputStream(uri).use { output ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            }
        }

    } catch (e: Exception) {
        Timber.e(e)
    }
    Log.e("wewerewr", "URI IS FUCKING AAAA $uri")
    return uri
}


private class ThreadSafeDateFormat(private val pattern: String) : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat = SimpleDateFormat(pattern, Locale.ROOT)
}

private val FORMAT_DATE: ThreadLocal<DateFormat> = ThreadSafeDateFormat("dd/MM/yyyy")

private val FORMAT_TIME: ThreadLocal<DateFormat> = ThreadSafeDateFormat("HH:mm:ss")

fun Calendar.getClearDate() = this.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.time

fun Date.formatDate(): String = FORMAT_DATE.get()!!.format(this)

fun Date.formatTime(): String = FORMAT_TIME.get()!!.format(this)
