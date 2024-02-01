package com.makovsky.mvi.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
    backClicked: () -> Unit,
    pokemon: String,
    pokemonClicked: (String) -> Unit,
    expanded: Boolean
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    var elementsVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = expanded){
        if (expanded) elementsVisible = false
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color.Red)
                    .animateContentSize(
                        animationSpec = tween(delayMillis = 400, durationMillis = 400),
                        finishedListener = { _, _ ->
                            if (expanded) {
                                navBack()
                                if (pokemon.isNotEmpty()) pokemonClicked(pokemon)
                            } else {
                                elementsVisible = true
                            }
                        }
                    )
                    .height(if (expanded) (screenHeightDp.dp / 2) - 11.dp else 56.dp)
                    .fillMaxWidth()
                    .debugInputPointer(LocalContext.current, timeMachine),
                horizontalArrangement = if (hasBackButton) Arrangement.SpaceBetween else Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (elementsVisible) {
                    if (hasBackButton) IconButton(
                        onClick = backClicked,
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
                    if (hasBackButton) Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(56.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .height(11.dp)
                    .fillMaxWidth()
                    .background(color = Color.Black)
                    .shadow(elevation = 10.dp),
            ) {}
        }
        Canvas(modifier = Modifier
            .size(67.dp)
            .align(Alignment.BottomCenter),
            onDraw = {
                drawArc(
                    color = Color.Black,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(
                        0f,
                        size.height / 2f),
                    size = size
                )
                drawArc(
                    color = Color.White,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(
                        size.width / 8f,
                        size.height / 1.59f),
                    size = size / 1.35f
                )
                drawArc(
                    color = Color.Black,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(
                        size.width / 4.5f,
                        size.height / 1.37f),
                    size = size / 1.8f
                )
            })
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
            navBack = {},
            backClicked = {},
            pokemon = "",
            pokemonClicked = {},
            expanded = false
        )
    }
}