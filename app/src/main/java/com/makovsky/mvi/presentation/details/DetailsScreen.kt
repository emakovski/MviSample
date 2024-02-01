package com.makovsky.mvi.presentation.details

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.makovsky.mvi.R
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.base.TimeTravelCapsule
import com.makovsky.mvi.data.remote.model.Stat
import com.makovsky.mvi.domain.entities.Artwork
import com.makovsky.mvi.domain.entities.Description
import com.makovsky.mvi.domain.entities.OtherSprites
import com.makovsky.mvi.domain.entities.PokemonDetails
import com.makovsky.mvi.domain.entities.PokemonSpecies
import com.makovsky.mvi.domain.entities.Sprites
import com.makovsky.mvi.domain.entities.Type
import com.makovsky.mvi.presentation.common.BottomView
import com.makovsky.mvi.presentation.common.Progress
import com.makovsky.mvi.presentation.common.Toolbar
import com.makovsky.mvi.ui.theme.Bug
import com.makovsky.mvi.ui.theme.Dark
import com.makovsky.mvi.ui.theme.Dragon
import com.makovsky.mvi.ui.theme.Electric
import com.makovsky.mvi.ui.theme.Fairy
import com.makovsky.mvi.ui.theme.Fighting
import com.makovsky.mvi.ui.theme.Fire
import com.makovsky.mvi.ui.theme.Flying
import com.makovsky.mvi.ui.theme.Ghost
import com.makovsky.mvi.ui.theme.Grass
import com.makovsky.mvi.ui.theme.Ground
import com.makovsky.mvi.ui.theme.Ice
import com.makovsky.mvi.ui.theme.MviSampleTheme
import com.makovsky.mvi.ui.theme.Normal
import com.makovsky.mvi.ui.theme.Poison
import com.makovsky.mvi.ui.theme.Psychic
import com.makovsky.mvi.ui.theme.Rock
import com.makovsky.mvi.ui.theme.Shadow
import com.makovsky.mvi.ui.theme.Steel
import com.makovsky.mvi.ui.theme.Unknown
import com.makovsky.mvi.ui.theme.Water
import kotlinx.coroutines.delay
import kotlin.random.Random

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
    var expanded by remember { mutableStateOf(true) }
    var backgroundColor by remember { mutableStateOf(Color.DarkGray) }

    LaunchedEffect(key1 = isLoading){
        if (!isLoading) expanded = false
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray)){
        data?.let {
            DetailsScreenContent(data = it, name = name)
            if (data is DetailsScreenItem.DetailsScreenPokemonInfoItem)
                backgroundColor = getBackgroundColor(data.pokemonDetails.types.first())
        }
        SparkEffect(brightenColor(backgroundColor, 1.3f))
        if (isLoading) Progress()
        Toolbar(
            timeMachine = timeMachine,
            title = "",
            hasBackButton = true,
            navBack = navBack,
            backClicked = { expanded = !expanded },
            pokemon = "",
            pokemonClicked = {},
            expanded = expanded
        )
        BottomView(expanded = expanded)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailsScreenContent(
    data: DetailsScreenItem,
    name: String
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
        var imageRatio by remember { mutableStateOf(1f) }
        val backgroundColor = getBackgroundColor(data.pokemonDetails.types.first())
        var rotationState by remember { mutableStateOf(0f) }

        LaunchedEffect(true) {
            while (true) {
                rotationState += 1f
                delay(16)
            }
        }

        LaunchedEffect(painter.state) {
            if (painter.state is AsyncImagePainter.State.Success) {
                imageRatio = painter.intrinsicSize.width / painter.intrinsicSize.height
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 70.dp,
                start = 4.dp,
                end = 4.dp,
                bottom = 70.dp
            )
            .background(
                color = Color.Yellow,
                shape = RoundedCornerShape(20.dp)
            )
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
            ){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(20.dp)
                    )
                )
                LazyColumn(
                    state = state,
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    stickyHeader {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    bottom = 8.dp,
                                )
                                .background(backgroundColor),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = capitalizeAfterHyphen(name),
                                    style = MaterialTheme.typography.h1,
                                )
                                Text(
                                    text = "${data.pokemonDetails.types.first().type.replaceFirstChar { it.uppercase() }} type pokémon",
                                    style = MaterialTheme.typography.body2,
                                )
                            }
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_pokeball),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(45.dp)
                                        .rotate(rotationState),
                                )
                                Text(
                                    text = "${data.pokemonDetails.baseExperience} HP",
                                    style = MaterialTheme.typography.h1,
                                )
                            }
                        }
                    }
                    item {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(2.dp)
                            )
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(10.dp)
                                    .background(
                                        color = Color.Yellow,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                text = data.pokemonDetails.order.toString(),
                                style = MaterialTheme.typography.h1,
                            )
                            Image(
                                painter = painter,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(imageRatio)
                                    .padding(4.dp)
                                    .background(
                                        color = backgroundColor.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(2.dp)
                                    ),
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }
                    item {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(2.dp)
                            )
                        ) {
                            data.pokemonDetails.stats.forEach {
                                if (it.stat.isNotEmpty()){
                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = capitalizeAfterHyphenAndEnter(it.stat),
                                            modifier = Modifier
                                                .weight(0.3f)
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 6.dp
                                                ),
                                            style = MaterialTheme.typography.body2,
                                        )
                                        LinearProgressIndicator(
                                            progress = it.baseStat.toFloat()/200,
                                            modifier = Modifier
                                                .weight(0.5f)
                                                .height(8.dp),
                                            backgroundColor = backgroundColor.copy(alpha = 0.3f),
                                            color = backgroundColor
                                        )
                                        Text(
                                            text = it.baseStat.toString(),
                                            modifier = Modifier
                                                .weight(0.2f)
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 6.dp
                                                ),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(2.dp)
                            )
                        ) {
                            data.pokemonSpecies.flavorTextEntries.forEach {
                                if (!it.flavorText.isNullOrEmpty()) Text(
                                    text = it.flavorText,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 2.dp
                                    ),
                                    style = MaterialTheme.typography.body1,
                                )
                            }
                        }
                    }
                }
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

private fun capitalizeAfterHyphenAndEnter(input: String): String {
    val words = input.split("-")
    val transformedWords = words.mapIndexed { index, word ->
        if (index == 0) {
            word.lowercase().replaceFirstChar { it.uppercase() }
        } else {
            "\n${word.lowercase().replaceFirstChar { it.uppercase() }}"
        }
    }
    return transformedWords.joinToString("")
}

private fun getBackgroundColor(type: Type): Color{
    return when (type.type) {
        "bug" -> Bug
        "dark" -> Dark
        "dragon" -> Dragon
        "electric" -> Electric
        "fairy" -> Fairy
        "fighting" -> Fighting
        "fire" -> Fire
        "flying" -> Flying
        "ghost" -> Ghost
        "grass" -> Grass
        "ground" -> Ground
        "ice" -> Ice
        "normal" -> Normal
        "poison" -> Poison
        "psychic" -> Psychic
        "rock" -> Rock
        "steel" -> Steel
        "water" -> Water
        "unknown" -> Unknown
        "shadow" -> Shadow
        else -> Color.White
    }
}

fun brightenColor(color: Color, factor: Float): Color {
    val red = (color.red * factor).coerceIn(0f, 1f)
    val green = (color.green * factor).coerceIn(0f, 1f)
    val blue = (color.blue * factor).coerceIn(0f, 1f)
    val alpha = color.alpha

    return Color(red, green, blue, alpha)
}

@Composable
fun SparkEffect(color: Color) {
    val sparks = remember { List(100) { generateRandomSpark() } }
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 1500f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)) {
        sparks.forEach { spark ->
            drawSpark(spark, offsetY % size.height, color)
        }
    }
}

fun generateRandomSpark(): Spark {
    return Spark(
        x = Random.nextFloat(),
        y = Random.nextFloat() * 1000f,
        radius = Random.nextFloat() * 2f + 2f,
        speed = Random.nextFloat() * 1.2f + 1f
    )
}

fun DrawScope.drawSpark(spark: Spark, offsetY: Float, color: Color) {
    val newY = (spark.y + offsetY * spark.speed) % size.height
    drawCircle(color, radius = spark.radius, center = Offset(spark.x * size.width, newY))
}

data class Spark(
    var x: Float,
    var y: Float,
    var radius: Float,
    var speed: Float
)

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
                    baseExperience = 112,
                    order = 35,
                    height = 4,
                    weight = 60,
                    types = listOf(Type(slot = 1, type = "electric")),
                    stats = listOf(
                        Stat(baseStat = 35, stat = "hp"),
                        Stat(baseStat = 55, stat = "attack"),
                        Stat(baseStat = 40, stat = "defence"),
                        Stat(baseStat = 50, stat = "special-attack"),
                        Stat(baseStat = 50, stat = "special-defence"),
                        Stat(baseStat = 90, stat = "speed"),
                    ),
                    sprites = Sprites(OtherSprites(Artwork("")))
                ),
                PokemonSpecies(
                    listOf(
                        Description("Hello World!"),
                        Description("Compose Sample"),
                    )
                )
            ),
            name = "PIKACHU"
        )
    }
}