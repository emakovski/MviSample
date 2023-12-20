package com.makovsky.mvi.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.makovsky.mvi.ui.theme.MviSampleTheme

@Composable
fun Progress() {
    Surface(color = Color.Red.copy(alpha = 0.2f)) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Red)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() { MviSampleTheme { Progress() } }