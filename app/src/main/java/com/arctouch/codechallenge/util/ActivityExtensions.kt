package com.arctouch.codechallenge.util

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.R

private var alertDialog: AlertDialog? = null

fun Activity.showProgress() {
    val builder = AlertDialog.Builder(this)
    val dialogView = layoutInflater.inflate(R.layout.progress_layout, null)
    builder.setView(dialogView)

    alertDialog = builder.create()
    alertDialog?.apply {
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        show()
    }
}

fun hideProgress() {
    alertDialog?.dismiss()
}

fun Activity.showErrorDialog(listener: DialogInterface.OnClickListener) {

    AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.error_dialog_close_button)) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setPositiveButton(getString(R.string.error_dialog_try_again_button), listener)
            .setTitle(R.string.error_dialog_title)
            .setMessage(R.string.error_dialog_message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
}

inline fun <reified V : ViewModel> AppCompatActivity.createViewModel(clazz: Class<V>): V {
    return ViewModelProviders.of(this).get(clazz)
}