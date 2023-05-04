package com.makovsky.mvi.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makovsky.mvi.R
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.domain.entities.Pokemon
import com.makovsky.mvi.utils.debugInputPointer
import javax.inject.Inject

@Composable
fun MainScreen (
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsState()

    Column {
     //Render toolbar
     Toolbar(viewModel.timeMachine)
     //Render screen content
     when {
        state.isLoading -> ContentWithProgress()
        state.data.isNotEmpty() -> MainScreenContent(state.data)
      }
    }
}

@Composable
private fun Toolbar(timeMachine: TimeCapsule<MainScreenState>) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .background(color = Color.Blue)
            .fillMaxWidth()
            .debugInputPointer(LocalContext.current, timeMachine),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
            text = stringResource(id = R.string.main_screen_title),
            color = Color.White,
            fontSize = 18.sp,
            style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Start,
        )
    }
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
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(start = 16.dp),
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
private fun ContentWithProgress() {
    Surface(color = Color.LightGray) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

data class MyState(
    val isShowProgressBar: Boolean,
    val error: Throwable? = null,
    val data: List<Pokemon>
)