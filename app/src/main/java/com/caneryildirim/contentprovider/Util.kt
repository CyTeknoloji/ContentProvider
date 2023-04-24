package com.caneryildirim.contentprovider

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted():Boolean{
    return ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
}

fun Activity.isShouldRationale(permission:String):Boolean{
    return ActivityCompat.shouldShowRequestPermissionRationale(this,permission)
}
