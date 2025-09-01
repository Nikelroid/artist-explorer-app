package com.nikelroid.artist_pedia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlin.text.split



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SearchScreen(navController: NavHostController,userData: UserObject= viewModel()) {


    var searchResults by remember { mutableStateOf<MutableList<ArtistSearchResult>>(mutableListOf()) }
    var isSearching by remember { mutableStateOf(false) }

    val searchScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var searchQuery by remember { mutableStateOf(TextFieldValue(userData.user.lastSearch)) }


    fun onClickSearch(runCount: Int = 0) {
        val searchQueryText = searchQuery.text.replace(" ", "+")
        if (searchQueryText.isNotEmpty()) {
            if (searchQueryText.length<3) isSearching = true
            searchScope.launch(Dispatchers.IO) {
                try {
                    val results = getArtistSearch(searchQueryText).toMutableStateList()
                    withContext(Dispatchers.Main) {
                        searchResults = results
                        isSearching = false
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        if (runCount<10){
                            val r: Int = 10*runCount
                        delay(r.toLong())
                        onClickSearch(runCount = runCount+1)
                        }else{
                            isSearching = false
                        }
                    }
                }
            }
        }
    }
    if (userData.user.lastSearch!="") onClickSearch()


    fun onArtistClick(artist: ArtistSearchResult) {
        userData.lastSearchSetter(searchQuery.text)
        navController.navigate("details/" +
                artist._links.self.href.split("/artists/")[1]+"/" +
                artist.title
        )
    }



    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(top = 55.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onClickSearch() }
                )

                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (searchQuery.text.length > 2) onClickSearch()
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    placeholder = {
                        Text(
                            "Search artists...", style = TextStyle(fontSize = 22.sp)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onClickSearch() }
                    )
                )

                if (searchQuery.text.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery = TextFieldValue("")
                        searchResults = mutableListOf()
                        userData.lastSearchSetter("")
                        navController.popBackStack("main", inclusive = false)

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear")
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when {
                    isSearching -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    searchResults.isNotEmpty() && searchQuery.text.length > 2 -> {
                        ArtistList(
                            artists = searchResults,
                            onArtistClick = { onArtistClick(it) }, user = userData,
                            snackbarHostState = snackbarHostState
                        )
                    }
                    !isSearching && searchResults.isEmpty() &&
                            searchQuery.text.length > 3-> {



                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Result Found",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }

                    }


                }

            }
        }
    }
        }
    }


@Composable
fun ArtistCard(
    artist: ArtistSearchResult,
    onClick: () -> Unit,
    isFavorite: Boolean=false,
    onStarClick: () -> Unit,
    isLoggedIn: Boolean = false,
) {
    var defaultImage = false
    var imageLink = artist._links.thumbnail.href
    if (!imageLink.startsWith("http")) {
        defaultImage = true
        imageLink = "https://upload.wikimedia.org/wikipedia/commons/f/fc/Artsy_logo.svg"
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            if (defaultImage) {
                val context = LocalContext.current

                val imageLoader = ImageLoader.Builder(context)
                    .components {
                        add(SvgDecoder.Factory())
                    }
                    .build()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageLink)
                            .crossfade(true)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = "Artist Image",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .fillMaxHeight()

                    )
                }
            }else {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageLink)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Artist Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (isLoggedIn) {
                var tempFav by remember { mutableStateOf(isFavorite) }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable(onClick = {
                            onStarClick()
                            tempFav = !tempFav
                        })
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (tempFav) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Remove from Favorites")
                    } else {
                        Icon(
                            imageVector = Icons.Default.StarOutline,
                            contentDescription = "Add to Favorites")
                }
            }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = artist.title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .weight(1f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View Artist",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }

}


@Composable
fun ArtistList(
    user: UserObject,
    artists: List<ArtistSearchResult>, onArtistClick: (ArtistSearchResult) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = artists,
            key = { artist -> artist._links.self.href }
        ) { artist ->

            ArtistCard(
                artist = artist,
                onClick = { onArtistClick(artist) }
                , isFavorite = user.searchFaveId(artist._links.self.href.split("/artists/")[1]) == true,
                onStarClick = {
                    scope.launch {
                        if (user.toggleFave(scope, artist._links.self.href.split("/artists/")[1]))
                        snackbarHostState.showSnackbar(
                            message = "Added to Favorites",
                            duration = SnackbarDuration.Short
                        )
                        else
                            snackbarHostState.showSnackbar(
                                message = "Removed from Favorites",
                                duration = SnackbarDuration.Short
                            )
                    }
                },
                isLoggedIn = (user.user.auth!="")
            )
        }
    }
}
