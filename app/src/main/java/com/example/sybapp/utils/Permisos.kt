package com.example.sybapp.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class Permisos {

    fun checkCamaraPermiso(activity:Activity): Boolean{
        return if(ContextCompat.checkSelfPermission(activity,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,android.Manifest.permission.CAMERA)){
                false
            }else{
                ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA),0)
                true
            }
        }else{
            true
        }
    }

    fun checkEscrituraPermiso(activity:Activity): Boolean{
        return if(ContextCompat.checkSelfPermission(activity,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,android.Manifest.permission.CAMERA)){
                false
            }else{
                ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
                true
            }
        }else{
            true
        }
    }
}