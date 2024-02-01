package com.makovsky.mvi.presentation.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.makovsky.mvi.R
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.base.TimeTravelCapsule
import com.makovsky.mvi.presentation.common.BottomView
import com.makovsky.mvi.presentation.common.Progress
import com.makovsky.mvi.presentation.common.Toolbar
import com.makovsky.mvi.ui.theme.MviSampleTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navToPokemon: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    MainScreenContentWithProgress(
        timeMachine = viewModel.timeMachine,
        isLoading = state.isLoading,
        allPokemonsLoaded = state.allPokemonsLoaded,
        loadMore = { viewModel.loadPokemons() },
        data = state.data,
        navToPokemon = navToPokemon
    )
}

@Composable
private fun MainScreenContentWithProgress(
    timeMachine: TimeCapsule<MainScreenState>,
    isLoading: Boolean,
    allPokemonsLoaded: Boolean,
    loadMore: () -> Unit,
    data: List<MainScreenItem>,
    navToPokemon: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }
    var pokemonToOpen by remember { mutableStateOf("") }

    LaunchedEffect(key1 = isLoading){
        if (!isLoading) expanded = false
    }
    Box(modifier = Modifier.fillMaxSize()){
        if (data.isNotEmpty()) MainScreenContent(
            pokemons = data,
            loadMore = loadMore,
            isLoading = isLoading,
            allPokemonsLoaded = allPokemonsLoaded,
            navToPokemon = { pokemon ->
                pokemonToOpen = pokemon
                expanded = !expanded
            }
        )
        if (isLoading) Progress()
        Toolbar(
            timeMachine = timeMachine,
            title = stringResource(id = R.string.main_screen_title),
            hasBackButton = false,
            navBack = {},
            backClicked = {},
            pokemon = pokemonToOpen,
            pokemonClicked = navToPokemon,
            expanded = expanded
        )
        BottomView(expanded = expanded)
    }
}

@Composable
private fun MainScreenContent(
    pokemons: List<MainScreenItem>,
    loadMore: () -> Unit,
    isLoading: Boolean,
    allPokemonsLoaded: Boolean,
    navToPokemon: (String) -> Unit,
) {
    val page = remember { mutableStateOf(1) }
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 66.dp,
                bottom = 66.dp
            )
            .background(Color.DarkGray)
    ) {
        itemsIndexed(pokemons) { index, item ->
            val animatableAlpha = remember { Animatable(0f) }
            val isVisible = remember {
                derivedStateOf {
                    state.firstVisibleItemIndex <= index
                }
            }

            LaunchedEffect(isVisible.value) {
                if (isVisible.value) {
                    animatableAlpha.animateTo(
                        1f, animationSpec = tween(durationMillis = 1000)
                    )

                }
            }
            when (item) {
                is MainScreenItem.MainScreenPokemonItem -> {
                    PokemonListItem(
                        item = item,
                        navToPokemon = navToPokemon,
                        alpha = animatableAlpha.value
                    )
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
    navToPokemon: (String) -> Unit,
    alpha: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = true) { navToPokemon(item.name) },
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.Red.copy(alpha = alpha)),
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
                MainScreenItem.MainScreenPokemonItem(
                    name = "Pikachu",
                    url = "",
                    number = "#001"
                ),
            ),
            navToPokemon = {}
        )
    }
}