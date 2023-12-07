package com.makovsky.mvi.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makovsky.mvi.R
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.base.TimeTravelCapsule
import com.makovsky.mvi.domain.entities.Pokemon
import com.makovsky.mvi.ui.theme.MviSampleTheme
import com.makovsky.mvi.utils.debugInputPointer
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()

    MainScreenContentWithProgress(
        timeMachine = viewModel.timeMachine,
        isLoading = state.isLoading,
        allPokemonsLoaded = state.allPokemonsLoaded,
        loadMore = { viewModel.loadPokemons() },
        data = state.data,
    )
}

@Composable
private fun MainScreenContentWithProgress(
    timeMachine: TimeCapsule<MainScreenState>,
    isLoading: Boolean,
    allPokemonsLoaded: Boolean,
    loadMore: () -> Unit,
    data: List<MainScreenItem>
) {
    Box(modifier = Modifier.fillMaxSize()){
        if (data.isNotEmpty()) MainScreenContent(
            pokemons = data,
            loadMore = loadMore,
            isLoading = isLoading,
            allPokemonsLoaded = allPokemonsLoaded
        )
        Toolbar(timeMachine)
        if (isLoading) ContentWithProgress()
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

@Composable
private fun MainScreenContent(
    pokemons: List<MainScreenItem>,
    loadMore: () -> Unit,
    isLoading: Boolean,
    allPokemonsLoaded: Boolean
) {
    val page = remember { mutableStateOf(1) }
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier.padding(top = 80.dp)
    ) {
        items(pokemons) { item ->
            when (item) {
                is MainScreenItem.MainScreenPokemonItem -> {
                    PokemonListItem(item = item)
                }
            }
        }
    }
    if (pokemons.isNotEmpty()) {
        LaunchedEffect(page.value) {
            if (page.value != 1 && !isLoading && !allPokemonsLoaded) loadMore()
        }

        LaunchedEffect(pokemons) {
            snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collectLatest { index ->
                    if (!isLoading && !allPokemonsLoaded && index != null && index == pokemons.size - 1) {
                        page.value++
                    }
                }
        }
    }
}

@Composable
private fun PokemonListItem(
    item: MainScreenItem.MainScreenPokemonItem,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                Color.White,
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.Red.copy(alpha = 0.5f)),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pokeball),
                    contentDescription = "",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Text(
                    text = item.number,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h1,
                    color = Color.White
                )
            }
            Text(
                text = item.name,
                modifier = Modifier.padding(
                    start = 16.dp,
                    bottom = 8.dp,
                    end = 8.dp
                ),
                style = MaterialTheme.typography.h5,
                color = Color.White
            )
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

@Preview(showSystemUi = true)
@Composable
private fun MainScreenContentPreview() {
    MviSampleTheme {
        MainScreenContentWithProgress(
            timeMachine = TimeTravelCapsule {},
            isLoading = false,
            allPokemonsLoaded = false,
            loadMore = {},
            data = listOf(
                MainScreenItem.MainScreenPokemonItem(
                    name = "Pikachu-Pikachu-Pikachu-Pikachu-Pikachu",
                    url = "",
                    number = "#10000"
                ),
                MainScreenItem.MainScreenPokemonItem(
                    name = "Pikachu",
                    url = "",
                    number = "#001"
                ),
                MainScreenItem.MainScreenPokemonItem(
                    name = "Pikachu",
                    url = "",
                    number = "#001"
                ),
                MainScreenItem.MainScreenPokemonItem(
                    name = "Pikachu",
                    url = "",
                    number = "#001"
                ),
                MainScreenItem.MainScreenPokemonItem(
                    name = "Pikachu",
                    url = "",
                    number = "#001"
                ),
            )
        )
    }
}