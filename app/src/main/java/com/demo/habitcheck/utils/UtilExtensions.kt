package com.demo.habitcheck.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.demo.habitcheck.R
import com.google.android.material.snackbar.Snackbar

object UtilExtensions {
    fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
        val intent = Intent(this, it)
        intent.putExtras(Bundle().apply(extras))
        startActivity(intent)
    }

    fun View.isVisible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun Context.myToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun mySnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    fun EditText.setTextEditable(text: String) {
        this.text = Editable.Factory.getInstance().newEditable(text)
    }

    fun updateDayUI(context: Context, textView: AppCompatTextView, isSelected: Boolean) {
        if (isSelected) {
            textView.background = ContextCompat.getDrawable(context, R.drawable.bg_selected)
            textView.setTextColor(context.resources.getColor(R.color.white, null))
        } else {
            textView.background = ContextCompat.getDrawable(context, R.drawable.bg_un_selected)
            textView.setTextColor(context.resources.getColor(R.color.black, null))
        }
    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}