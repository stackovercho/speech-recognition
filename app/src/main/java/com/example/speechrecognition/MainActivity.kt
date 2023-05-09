package com.example.speechrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

const val MA = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)

        // set up everything for speech recognition
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        val handler = SpeechHandler()
        speechRecognizer.setRecognitionListener(handler)

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
        Log.d(MA, "inside start listening")
        speechRecognizer.startListening(intent)
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

    inner class SpeechHandler : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.d(MA, "inside onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            Log.d(MA, "inside onBeginningOfSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            // Log.d(MA, "inside onRmsChanged")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d(MA, "inside onBufferReceived")
        }

        override fun onEndOfSpeech() {
            Log.d(MA, "inside onEndOfSpeech")
        }

        override fun onError(error: Int) {
            Log.d(MA, "inside onError $error")
            listen()
        }

        override fun onResults(results: Bundle?) {
            Log.d(MA, "inside onResults")
            if (results != null) {
                val speechResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val speechScores = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
                Log.d(MA, "results: $speechResults")
                Log.d(MA, "scores: $speechScores")
                val i = 0
                if (speechResults != null && speechScores != null) {
                    for (word in speechResults) {
                        Log.d(MA, "result: $word")
                        if (i < speechScores.size)
                            Log.d(MA, "confidence score = ${speechScores[i]}")
                    }
                    tv.text = speechResults[0]
                } else {
                    Log.w(MA, "no results!!")
                }
            } else {
                Log.w(MA, "params is null!!  no results possible")
            }
            listen()
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d(MA, "inside onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d(MA, "inside onEvent")
        }
    }
}