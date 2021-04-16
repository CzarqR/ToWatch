package com.myniprojects.towatch.utils.ext

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun CoordinatorLayout.showSnackbar(
    message: String,
    buttonText: String? = null,
    action: () -> Unit = {},
    length: Int = Snackbar.LENGTH_LONG,
    gravity: Int = Gravity.TOP
)
{
    val s = Snackbar
        .make(this, message, length)

    buttonText?.let {
        s.setAction(it) {
            action()
        }
    }

    val params = s.view.layoutParams as CoordinatorLayout.LayoutParams
    params.gravity = gravity
    s.view.layoutParams = params
    s.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

    s.show()
}

fun Activity.hideKeyboard()
{
    if (this.window != null)
    {
        val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)

        //remove focus from EditText
        findViewById<View>(android.R.id.content).clearFocus()
    }
}

fun Fragment.hideKeyboard()
{
    requireActivity().hideKeyboard()
}