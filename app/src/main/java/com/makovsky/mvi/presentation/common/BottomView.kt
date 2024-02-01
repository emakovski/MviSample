package com.makovsky.mvi.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.makovsky.mvi.ui.theme.MviSampleTheme

@Composable
fun BottomView(
    expanded: Boolean
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .shadow(elevation = 10.dp)
                        .height(11.dp)
                        .fillMaxWidth()
                        .background(color = Color.Black),
                ) {}
                Row(
                    modifier = Modifier
                        .background(color = Color.White)
                        .animateContentSize(
                            animationSpec = tween(delayMillis = 400, durationMillis = 400),
                        )
                        .height(if (expanded) (screenHeightDp.dp / 2) - 11.dp else 56.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                }
            }
            Canvas(modifier = Modifier
                .size(67.dp)
                .align(Alignment.TopCenter),
                onDraw = {
                    drawArc(
                        color = Color.Black,
                        startAngle = -180f,
                        sweepAngle = -180f,
                        useCenter = true,
                        topLeft = Offset(
                            0f,
                            -size.height / 2f),
                        size = size
                    )
                    drawArc(
                        color = Color.White,
                        startAngle = -180f,
                        sweepAngle = -180f,
                        useCenter = true,
                        topLeft = Offset(
                            size.width / 8f,
                            -size.height / 2.7f),
                        size = size / 1.35f
                    )
                    drawArc(
                        color = Color.Black,
                        startAngle = -180f,
                        sweepAngle = -180f,
                        useCenter = true,
                        topLeft = Offset(
                            size.width / 4.5f,
                            -size.height / 3.56f),
                        size = size / 1.8f
                    )
                })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    MviSampleTheme {
        BottomView(
            expanded = false
        )
    }
}