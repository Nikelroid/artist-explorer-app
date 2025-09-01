package com.nikelroid.artist_pedia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikelroid.artist_pedia.ui.theme.AppTheme
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        RetrofitClient.initialize(applicationContext)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainAppNav()
            }
        }
    }
}



@Composable
fun MainAppNav(userData: UserObject = viewModel()) {

    runBlocking {
        fetchUser(userData)
    }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { ArtistSearchApp(navController = navController,userData) }
        composable("login") { LoginScreen(navController = navController,userData) }
        composable("register") { RegScreen(navController = navController,userData) }
        composable("search") { SearchScreen(navController = navController,userData) }
        composable("details/{artistId}/{artistName}") {
                backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("artistId") ?: ""
            val artistName = backStackEntry.arguments?.getString("artistName") ?: ""
            ArtistDetailScreen(navController = navController,
                userData,artistId = artistId,artistName = artistName) }
    }
}

@SuppressLint("SimpleDateFormat", "UnusedMaterial3ScaffoldPaddingParameter",
    "UnrememberedMutableState"
)
@Composable
fun ArtistSearchApp(navController: NavHostController,userData: UserObject= viewModel()) {
    val snackbarHostState by mutableStateOf( SnackbarHostState())

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
                    .padding(top = 60.dp, start = 2.dp, end = 2.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 14.dp),
                    text = "Artist Search",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal

                )

                Row(
                    modifier = Modifier
                        .padding(end = 4.dp)
                ) {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search"
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate("login")
                    }) {
                        if (userData.user.avatar == "") {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Profile"
                            )
                        } else {
                            val scope = rememberCoroutineScope()
                            var expanded by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .padding(0.dp)
                            ) {
                                val context = LocalContext.current
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(userData.user.avatar)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 6.dp, vertical = 6.dp)
                                        .clip(CircleShape)
                                        .clickable(onClick = { expanded = !expanded })
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Log out", color = MaterialTheme.colorScheme.onSurface) },
                                        onClick = {
                                            scope.launch {
                                                val out = postLogOut()
                                                if (out == "true") {
                                                    expanded = false
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Logged out successfully",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }
                                                    userData.clearWithDelay(750, scope)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Couldn't log out",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "Delete account", color = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        onClick = {
                                            scope.launch {
                                                val deleted = postDeleteAccount()
                                                if (deleted=="true") {
                                                    expanded = false
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Deleted user successfully",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }
                                                    userData.clearWithDelay(750, scope)

                                                }else{

                                                        Toast.makeText(
                                                            context,
                                                            "Couldn't delete account",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                    )
                                }
                            }

                        }

                    }
                }
            }


            val today = remember {
                LocalDate.now().format(
                    DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
                )
            }
            Text(
                text = today,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 14.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceBright),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 3.dp),
                        text = "Favorites",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,

                        )
                }

            }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    if (userData.user.auth == "") {
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                navController.navigate("login")
                            },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "Log in to see favorites",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    } else {

                        fun onArtistClick(artist: Favorite) {
                            navController.navigate("details/${artist.artistId}/${artist.title}")
                        }

                        val favAbsTimes: MutableList<String> = mutableListOf()
                        var favRelTimes = remember { mutableStateListOf<String>() }
                        for (item in userData.user.favorites) {
                            favAbsTimes.add(item.time)
                        }
                        val timeUtil = TimeUtils(favAbsTimes)
                        favRelTimes.addAll(timeUtil.getUpdatedFavTime())
                        @Composable
                        fun launchUpdate() {
                            LaunchedEffect(userData.user.favorites) {
                                withContext(Dispatchers.IO) {
                                    while (isActive) {
                                        try {
                                            val startTime = System.currentTimeMillis()
                                            val updatedTimes = timeUtil.getUpdatedFavTime()
                                            withContext(Dispatchers.Main) {
                                                favRelTimes.clear()
                                                favRelTimes.addAll(updatedTimes)
                                            }
                                            val executionTime =
                                                System.currentTimeMillis() - startTime
                                            val remainingTime = maxOf(0L, 1000 - executionTime)
                                            delay(remainingTime)
                                        } catch (e: Exception) {
                                            println("Update failed: ${e.message}")
                                        }
                                    }
                                }
                            }
                        }


                        launchUpdate()

                        if (userData.user.favorites.isEmpty()){
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
                                    text = "No favorites",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }else {
                            ArtistCardList(
                                favorites = userData.user.favorites,
                                onArtistClick = { artist -> onArtistClick(artist) },
                                times = favRelTimes
                            )
                        }
                    }


                    val handler = LocalUriHandler.current
                    Text(
                        text = "Powered by Artsy",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp, top = 28.dp)
                            .clickable { handler.openUri("https://www.artsy.net/") }
                    )

                }
            }
        }
    }
}


@Composable
fun ArtistCard(
    index: Int,
    favorite: Favorite,
    onCardClick: (Favorite) -> Unit,
    modifier: Modifier = Modifier,
    times: MutableList<String>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {onCardClick(favorite) }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = favorite.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    var dates: String = ""
                    var nationality: String = ""

                    if (favorite.dates!=null) dates = favorite.dates
                    if (favorite.nationality!=null) nationality = favorite.nationality

                    var separate = ""
                    if (dates.isNotEmpty() && nationality.isNotEmpty()){ separate = ", " }
                    val yearsText = nationality + separate + dates
                    Text(
                        text = yearsText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (times.isEmpty()) "" else times[index],
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View Artist",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistCardList(
    favorites: List<Favorite>,
    onArtistClick: (Favorite) -> Unit,
    modifier: Modifier = Modifier,
    times: MutableList<String>
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(favorites) { index, favorite ->
            ArtistCard(
                index = index,
                favorite = favorite,
                onCardClick = onArtistClick,
                times= times
            )
        }
    }
}

