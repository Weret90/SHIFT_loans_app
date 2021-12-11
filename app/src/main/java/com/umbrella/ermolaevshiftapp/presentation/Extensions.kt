package com.umbrella.ermolaevshiftapp.presentation

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_PATTERN = "dd.MM.yy HH:mm"

fun View.showSnackBar(text: String?, actionText: String, action: (View) -> Unit) {
    Snackbar.make(this, text.toString(), Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText, action).show()
}

fun Context?.showToast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun EditText.getStringText(): String {
    return this.text.toString()
}

fun Date.convertToString(): String {
    val sdf = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    return sdf.format(this)
}