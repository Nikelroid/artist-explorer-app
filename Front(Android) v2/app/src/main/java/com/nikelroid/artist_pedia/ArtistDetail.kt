package com.nikelroid.artist_pedia

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ArtistDetailScreen(navController: NavHostController, userData:UserObject= viewModel(), artistId: String, artistName: String) {
    val context = LocalContext.current
    var searchResults by remember { mutableStateOf(ArtistBioMaker()) }
    var similarResults by remember { mutableStateOf<List<ArtistBio>>(emptyList()) }
    var artworkResults by remember { mutableStateOf<List<Artwork>>(emptyList()) }
    var isSearchingArtwork by remember { mutableStateOf(false) }
    var isSearchingDetails by remember { mutableStateOf(false) }
    var isSearchingSimilar by remember { mutableStateOf(false) }

    val searchScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(artistId) {
        if (userData.user.auth!=""){

            scope.launch(Dispatchers.IO) {
                try {
                    isSearchingSimilar=true
                    val results = getSimilarData(artistId)
                    withContext(Dispatchers.Main) {
                        similarResults = results
                        isSearchingSimilar=false
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        scope.launch(Dispatchers.IO) {
            try {
                isSearchingDetails=true
                val results = getDescription(artistId)
                withContext(Dispatchers.Main) {
                   searchResults = results
                    isSearchingDetails=false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        scope.launch(Dispatchers.IO) {
            try {
                isSearchingArtwork=true
                val results = getArtworks(artistId)
                withContext(Dispatchers.Main) {
                    artworkResults=results
                    isSearchingArtwork=false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(top = 60.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "arrow",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { navController.popBackStack() }
                )

                Text(
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    text = artistName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                val scope = rememberCoroutineScope()
                var tempFav by remember { mutableStateOf(userData.searchFaveId(artistId)) }
                if (userData.user.auth != "") {
                    if (tempFav) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    tempFav = !tempFav
                                    scope.launch {
                                        if (userData.toggleFave(scope, artistId))
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
                                }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.StarOutline,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    tempFav = !tempFav
                                    scope.launch {

                                        if (userData.toggleFave(scope, artistId))
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

                                }
                        )

                    }
                }
            }


            var selectedTabIndex by remember { mutableIntStateOf(0) }

            var tabLabels = listOf("Details", "Artworks","Similar")
            var tabIcons = listOf(Icons.Outlined.Info,
                Icons.Outlined.AccountBox,Icons.Outlined.PersonSearch)
            if (userData.user.auth==""){
                tabLabels = tabLabels.dropLast(1)
                tabIcons = tabIcons.dropLast(1)
            }
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        tabPositions.forEachIndexed { index, position ->
                            if (index < tabPositions.size) {
                                Box(
                                    Modifier
                                        .tabIndicatorOffset(position)
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.surfaceBright)
                                )
                            }
                        }

                        Box(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(3.dp)
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                    }
                },
                divider = {
                    Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.background)
                }
            ) {

                tabLabels.forEachIndexed { index, title ->
                        Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                            },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> ArtistDetailsTab(searchResults,isSearchingDetails)
                1 -> ArtistArtworksTab(artworkResults,isSearchingArtwork)
                2 -> SimilarTab(similarResults,navController,userData,isSearchingSimilar,snackbarHostState)
            }
        }
        }
    }
}

@Composable
fun ArtistDetailsTab(artist: ArtistBio,isSearchingDetails: Boolean) {

        if (isSearchingDetails) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading...")
                }
            }
        }else{

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                var birthYear: String = ""
                var deathYear: String = ""
                var bioGraphy: String = ""
                var nationality: String = ""

                if (artist.birthday != null) birthYear = artist.birthday
                if (artist.deathday != null) deathYear = artist.deathday
                if (artist.biography != null) bioGraphy = artist.biography
                if (artist.nationality != null) nationality = artist.nationality

                birthYear =
                    if (birthYear.isNotEmpty() && birthYear.length >= 4) birthYear.substring(
                        0,
                        4
                    ) else ""
                deathYear =
                    if (deathYear.isNotEmpty() && deathYear.length >= 4) deathYear.substring(
                        0,
                        4
                    ) else ""
                var separate = ""
                if (birthYear.isNotEmpty() && deathYear.isNotEmpty()) {
                    separate = " - "
                }
                val yearsText = birthYear + separate + deathYear

                Text(
                    text = buildString {
                        append(nationality)
                        if ((birthYear.isNotEmpty() || birthYear.isNotEmpty()) && nationality.isNotEmpty()) {
                            append(", ")
                        }
                        append(yearsText)
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                )


                Text(
                    text = bioGraphy,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }
        }
    }



@Composable
fun SimilarTab(similarList : List<ArtistBio>,navController: NavHostController,
               userData: UserObject,isSearchingSimilar: Boolean,snackbarHostState:
               SnackbarHostState) {

    fun onArtistClick(artist: ArtistBio) {
        navController.navigate("details/" + artist.id+"/" + artist.name
        )
    }
    when {
        isSearchingSimilar -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading...")
                }
            }
        }

        similarList.isNotEmpty() -> {
            SimilarArtistList(
                artists = similarList,
                onArtistClick = { onArtistClick(it) }, user = userData,
                snackbarHostState = snackbarHostState
            )
        }
        !similarList.isNotEmpty() -> {
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
                text = "No Similar Artists",
                fontSize = 18.sp,
                color = Color(0xFF001E3C),
                textAlign = TextAlign.Center
            )
        }
    }
    }
}

@Composable
fun ArtistArtworksTab(artworks: List<Artwork>, isSearchingArtist: Boolean) {
    var selectedArtworkId by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        when {
            isSearchingArtist -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading...")
                    }
                }

            }
            artworks.isNotEmpty() -> {
                ArtworkList(
                    artworks = artworks.toList(),
                    onArtworkClick = { artwork ->
                        selectedArtworkId = artwork.id
                        showDialog = true
                    }
                )
            }
            !artworks.isNotEmpty() -> {
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
                        text = "No Artworks",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (showDialog && selectedArtworkId != null) {
            ArtworkDialog(
                artworkId = selectedArtworkId!!,
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun ArtworkCard(artwork: Artwork, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artwork._links.thumbnail.href)
                    .crossfade(true)
                    .build(),
                contentDescription = artwork.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${artwork.title}, ${artwork.date}",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                textAlign = TextAlign.Center
            )

            androidx.compose.material3.Button(
                onClick = onClick,
                shape = RoundedCornerShape(76.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(bottom = 18.dp).height(38.dp),
            ) {
                Text(
                    text = "View categories",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 0.dp)
                )
            }
        }
    }
}

@Composable
fun ArtworkList(artworks: List<Artwork>, onArtworkClick: (Artwork) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues()
    ) {
        items(
            items = artworks,
            key = { artist -> artist._links.self.href }
        ) { artwork ->
            ArtworkCard(
                artwork = artwork,
                onClick = { onArtworkClick(artwork) }
            )
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworkDialog(artworkId: String, onDismiss: () -> Unit) {
    val searchScope = rememberCoroutineScope()
    var geneResults by remember { mutableStateOf<List<Gene>>(emptyList()) }
    var catResReady by remember { mutableStateOf(false) }
    val context = LocalContext.current

    searchScope.launch(Dispatchers.IO) {
        try {
            catResReady = false
            val results = getCategories(artworkId)
            withContext(Dispatchers.Main) {
                geneResults = results
                catResReady=true
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                catResReady=true
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp).background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.headlineSmall
                    , modifier = Modifier.padding(vertical = 15.dp, horizontal = 6.dp)
                )


                if (geneResults.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE })

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(480.dp)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 18.dp),
                            userScrollEnabled = true,
                        ) { page ->
                            val index = abs(page) % geneResults.size
                            val gene = geneResults[index]

                            Card(
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(horizontal = 3.dp, vertical = 2.dp)
                                    .fillMaxHeight(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceBright
                                ),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(170.dp)
                                            .background(MaterialTheme.colorScheme.secondary)
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(gene._links.thumbnail.href)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = gene.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.Center)
                                        )
                                    }

                                    Text(
                                        text = gene.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                                    )
                                    var desc = gene.description
                                    val regex = Regex("\\[(.*?)]\\((https?://|/)[^)]*\\)")
                                    desc = regex.replace(desc) { matchResult -> matchResult.groupValues[1] }
                                    val scrollState = rememberScrollState()
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .padding(bottom = 16.dp)
                                            .weight(1f)
                                            .verticalScroll(scrollState)
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = {
                                    searchScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                },
                                modifier = Modifier.size(60.dp).
                                offset(x = (-20).dp),
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    "Previous"
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    searchScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                                modifier = Modifier.size(60.dp).
                                offset(x = (20).dp),
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    "Next"
                                )
                            }
                        }
                    }
                } else if(catResReady){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Text("No categories available")
                    }
                }


                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    androidx.compose.material3.Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(72.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.padding(vertical = 18.dp).height(36.dp),
                    ) {
                        Text(
                            text = "Close",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 0.dp)
                        )
                    }
                }
            }
        }
    }
}



private fun getCategoryData(
    artworkId: String,
    scope: CoroutineScope,
    context: Context,
    onSuccess: (List<Gene>) -> Unit
) {

}

@Composable
fun SimilarArtistCard(
    artist: ArtistBio,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                        .background(MaterialTheme.colorScheme.primary),
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
                            contentDescription = "Remove from Favorites",
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.StarOutline,
                            contentDescription = "Add to Favorites",
                        )
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
                        text = artist.name,
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
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun SimilarArtistList(
    user: UserObject,
    artists: List<ArtistBio>, onArtistClick: (ArtistBio) -> Unit,
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

            SimilarArtistCard(
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