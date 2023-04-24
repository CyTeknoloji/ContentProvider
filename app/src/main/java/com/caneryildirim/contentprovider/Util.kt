package com.caneryildirim.contentprovider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted():Boolean{
    return ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
}
