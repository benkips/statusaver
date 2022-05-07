package com.mabnets.statussaverquick.Utilz

import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.material.snackbar.Snackbar
import com.mabnets.statussaverquick.MainActivity
import com.mabnets.statussaverquick.R


fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()

}

fun Context.showmessages(
    Title: String,
    message: String,
    retry: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setTitle(Title)
        setMessage(message)
        setCancelable(false)
        setPositiveButton("Ok") { _, _ -> retry?.invoke() }
    }.show()
}

fun isValidMobile(field: String): Boolean {
    return Patterns.PHONE.matcher(field).matches()
}

fun isValidEmail(field: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(field).matches()
}

fun Context.showPermissionRequestExplanation(
    permission: String,
    message: String,
    retry: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {

        setTitle("$permission Required")
        setMessage(message)
        setPositiveButton("Ok") { _, _ -> retry?.invoke() }
    }.show()
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
fun Context.permissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun View.showmessages(
    Title: String,
    message: String,
    retry: (() -> Unit)? = null
) {
    AlertDialog.Builder(this.context).apply {
        setTitle(Title)
        setMessage(message)
        setPositiveButton("Ok") { _, _ -> retry?.invoke() }
    }.show()
}

fun View.showPermissionRequestExplanation(
    permission: String,
    message: String,
    retry: (() -> Unit)? = null
) {
    AlertDialog.Builder(this.context).apply {
        setTitle("$permission Required")
        setMessage(message)
        setPositiveButton("Ok") { _, _ -> retry?.invoke() }
    }.show()
}





fun Context.gotomy(x:Int): PendingIntent {
//        navController.navigate(R.id.live)
    val pendingintent= NavDeepLinkBuilder(this.applicationContext)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.mobile_navigation)
        .setDestination(x)
        .createPendingIntent()
    return pendingintent
}



fun Context.safeInt(text: String): Int? {
    return text.toIntOrNull()
    }

