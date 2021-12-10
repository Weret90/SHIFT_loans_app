package com.umbrella.ermolaevshiftapp.presentation

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

private const val PARSER_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
private const val FORMATTER_PATTERN = "dd.MM.yyyy HH:mm"

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

fun convertTime(time: String): String {
    val parser = SimpleDateFormat(PARSER_PATTERN, Locale.getDefault())
    val formatter = SimpleDateFormat(FORMATTER_PATTERN, Locale.getDefault())
    return formatter.format(parser.parse(time))
}