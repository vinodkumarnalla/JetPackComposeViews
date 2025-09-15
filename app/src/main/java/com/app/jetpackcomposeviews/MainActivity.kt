package com.app.jetpackcomposeviews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.jetpackcomposeviews.ui.theme.JetPackComposeViewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetPackComposeViewsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun MainContent(value: PaddingValues) {

    Column(
        Modifier
            .padding(value),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ShimmerPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(200.dp)
        )
        SwipeToUnlockSwitch(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
        }
    }
}
