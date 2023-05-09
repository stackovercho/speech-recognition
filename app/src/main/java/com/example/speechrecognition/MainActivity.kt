package com.example.speechrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

const val MA = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val voiceGranted: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        if (voiceGranted == PackageManager.PERMISSION_GRANTED) {
            Log.d(MA, "voice permission granted")
            listen()
        } else {
            // ask voice permission
            val contract = ActivityResultContracts.RequestPermission()
            val callback = PermissionResults()
            permissionLauncher = registerForActivityResult(contract, callback)
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun listen() {

    }

    inner class PermissionResults : ActivityResultCallback<Boolean> {
        override fun onActivityResult(result: Boolean?) {
            if (result != null && result == true) {
                listen()
            } else {
                Log.w(MA, "permission not granted by user")
            }
        }
    }
}