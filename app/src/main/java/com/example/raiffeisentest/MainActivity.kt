package com.example.raiffeisentest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.raiffeisentest.presentation.theme.RaiffeisenTestTheme
import com.example.raiffeisentest.presentation.users.UsersScreen
import com.example.raiffeisentest.presentation.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel

/** Hosts the users feature. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RaiffeisenTestTheme {
                val usersViewModel: UsersViewModel = koinViewModel()
                UsersScreen(usersViewModel = usersViewModel)
            }
        }
    }
}
