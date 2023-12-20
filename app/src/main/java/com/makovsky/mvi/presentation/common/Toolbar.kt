package com.makovsky.mvi.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.base.TimeTravelCapsule
import com.makovsky.mvi.base.UiState
import com.makovsky.mvi.ui.theme.MviSampleTheme
import com.makovsky.mvi.utils.debugInputPointer

@Composable
fun Toolbar(
    timeMachine: TimeCapsule<out UiState>,
    title: String,
    hasBackButton: Boolean,
    navBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .background(color = Color.Red)
                .fillMaxWidth()
                .debugInputPointer(LocalContext.current, timeMachine),
            horizontalArrangement = if (hasBackButton) Arrangement.SpaceBetween else Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasBackButton) IconButton(
                onClick = navBack,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center
            )
            if (hasBackButton) Spacer(modifier = Modifier.fillMaxHeight().width(56.dp))
        }
        Row(
            modifier = Modifier
                .height(22.dp)
                .fillMaxWidth()
                .background(color = Color.DarkGray),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Canvas(modifier = Modifier
                .size(48.dp),
                onDraw = {
                    drawCircle(Color.DarkGray, radius = 63f)
                    drawCircle(Color.White, radius = 42f)
                    drawCircle(Color.DarkGray, radius = 21f)
                })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    MviSampleTheme {
        Toolbar(
            timeMachine = TimeTravelCapsule {},
            title = "Title",
            hasBackButton = true,
            navBack = {}
        )
    }
}