package com.makovsky.mvi.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makovsky.mvi.R
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.domain.entities.Pokemon
import com.makovsky.mvi.utils.debugInputPointer

@Composable
fun MainScreen (
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsState()

    Column {
        Toolbar(viewModel.timeMachine)
        when {
            state.isLoading -> ContentWithProgress()
            state.data.isNotEmpty() -> MainScreenContent(state.data)
        }
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            .offset(0.dp, 43.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        PokeballShape()
    }
}

@Composable
private fun Toolbar(timeMachine: TimeCapsule<MainScreenState>) {
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.main_screen_title),
                color = Color.White,
                fontSize = 22.sp,
                style = MaterialTheme.typography.h1
            )
        }
        Row(
            modifier = Modifier
                .height(22.dp)
                .fillMaxWidth()
                .background(color = Color.DarkGray),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ){}
    }
}

@Composable
fun PokeballShape(){
    Canvas(modifier = Modifier
        .size(48.dp),
        onDraw = {
        drawCircle(Color.DarkGray, radius = 63f)
        drawCircle(Color.White, radius = 42f)
        drawCircle(Color.DarkGray, radius = 21f)
    })
}

@Composable
private fun MainScreenContent(
    pokemons: List<MainScreenItem>,
) {
    Box {
        LazyColumn(content = {
            itemsIndexed(pokemons) { index, item ->
                when (item) {
                    is MainScreenItem.MainScreenPokemonItem -> {
                        PokemonListItem(item = item, index)
                    }
                }
            }
        })
    }
}

@Composable
private fun PokemonListItem(
    item: MainScreenItem.MainScreenPokemonItem,
    index: Int,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Red.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ),
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red.copy(alpha = 0.5f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pokeball),
                    contentDescription = "",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Text(
                    text = item.name,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h1,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = item.number,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h1,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ContentWithProgress() {
    Surface(color = Color.Red.copy(alpha = 0.2f)) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Red)
        }
    }
}

data class MyState(
    val isShowProgressBar: Boolean,
    val error: Throwable? = null,
    val data: List<Pokemon>
)