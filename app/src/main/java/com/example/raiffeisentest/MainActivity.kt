package com.example.raiffeisentest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.raiffeisentest.presentation.AppRouter

/** Android entry point that delegates application UI ownership to [AppRouter]. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppRouter()
        }
    }
}
