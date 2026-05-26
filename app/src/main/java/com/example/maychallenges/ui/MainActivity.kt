package com.example.maychallenges.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.maychallenges.ui.theme.MayChallengesTheme
import com.example.maychallenges.ui.oneshotaction.OrderSummaryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MayChallengesTheme {
                OrderSummaryScreen()
            }
        }
    }
}
