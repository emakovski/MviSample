package com.makovsky.mvi.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.makovsky.mvi.R
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.base.TimeTravelCapsule
import com.makovsky.mvi.domain.entities.Artwork
import com.makovsky.mvi.domain.entities.Description
import com.makovsky.mvi.domain.entities.OtherSprites
import com.makovsky.mvi.domain.entities.PokemonDetails
import com.makovsky.mvi.domain.entities.PokemonSpecies
import com.makovsky.mvi.domain.entities.Sprites
import com.makovsky.mvi.presentation.common.Progress
import com.makovsky.mvi.presentation.common.Toolbar
import com.makovsky.mvi.ui.theme.MviSampleTheme

@Composable
fun DetailsScreen (
    pokemon: String,
    viewModel: DetailsViewModel,
    navBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = pokemon) {
        viewModel.launchPokemon(pokemon)
    }

    DetailsScreenContentWithProgress(
        timeMachine = viewModel.timeMachine,
        isLoading = state.isLoading,
        navBack = navBack,
        data = state.data,
        name = pokemon
    )
}

@Composable
private fun DetailsScreenContentWithProgress(
    timeMachine: TimeCapsule<DetailsScreenState>,
    isLoading: Boolean,
    navBack: () -> Unit,
    data: DetailsScreenItem?,
    name: String
) {
    Box(modifier = Modifier.fillMaxSize()){
        data?.let { DetailsScreenContent(data = it) }
        Toolbar(
            timeMachine = timeMachine,
            title = capitalizeAfterHyphen(name),
            hasBackButton = true,
            navBack = navBack
        )
        if (isLoading) Progress()
    }
}

@Composable
private fun DetailsScreenContent(
    data: DetailsScreenItem,
) {
    if (data is DetailsScreenItem.DetailsScreenPokemonInfoItem){
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = data.pokemonDetails.sprites.other.artwork.frontDefault)
                .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
            }).build()
        )
        val tempPainter = painterResource(id = R.drawable.pikachu)
        val state = rememberLazyListState()
        var imageRatio by remember { mutableStateOf(0.5f) }

        LaunchedEffect(painter.state) {
            if (painter.state is AsyncImagePainter.State.Success) {
                imageRatio = painter.intrinsicSize.width / painter.intrinsicSize.height
            }
        }

        LazyColumn(
            state = state,
            modifier = Modifier.padding(top = 80.dp)
        ) {
            item {
                Image(
                    painter = painter,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(imageRatio),
                    contentScale = ContentScale.Fit,
                    )
            }
            items(data.pokemonSpecies.flavorTextEntries){
                if (!it.flavorText.isNullOrEmpty()) Text(
                    text = it.flavorText,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        bottom = 8.dp,
                        end = 8.dp
                    ),
                    style = MaterialTheme.typography.h5,
                )
            }
        }
    }
}

private fun capitalizeAfterHyphen(input: String): String {
    val words = input.split("-")
    val transformedWords = words.mapIndexed { index, word ->
        if (index == 0) {
            word.lowercase().replaceFirstChar { it.uppercase() }
        } else {
            "-${word.lowercase().replaceFirstChar { it.uppercase() }}"
        }
    }
    return transformedWords.joinToString("")
}

@Preview(showSystemUi = true)
@Composable
private fun MainScreenContentPreview() {
    MviSampleTheme {
        DetailsScreenContentWithProgress(
            timeMachine = TimeTravelCapsule {},
            isLoading = false,
            navBack = {},
            data = DetailsScreenItem.DetailsScreenPokemonInfoItem(
                PokemonDetails(
                    id = 23,
                    name = "Pikachu",
                    sprites = Sprites(OtherSprites(Artwork("")))
                ),
                PokemonSpecies(
                    listOf(
                        Description("Hello World!"),
                        Description("Compose Sample"),
                    )
                )
            ),
            name = "PIKACHU-PIKACHU-PIKACHU"
        )
    }
}