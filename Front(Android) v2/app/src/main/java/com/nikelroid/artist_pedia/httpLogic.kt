package com.nikelroid.artist_pedia

import kotlinx.coroutines.DelicateCoroutinesApi

val appAPI = RetrofitClient.api
var artistList = mutableListOf<ArtistSearchResult>()


@OptIn(DelicateCoroutinesApi::class)
suspend fun getArtistSearch(searchQuery: String): List<ArtistSearchResult> {
    try {
        val result = appAPI.searchArtist(searchQuery)
    artistList = mutableListOf<ArtistSearchResult>()
    result.forEach { item ->
            artistList.add(item)
        }
    return artistList.toList()
    } catch (e: Exception) {
        println("Retrieve data [getArtistSearch] failed: ${e.message}")
        return emptyList()
    }
    }

suspend fun getDescription(searchQuery: String): ArtistBio {
    try {
    val result = appAPI.getArtist(searchQuery)
    println("Detail Result:$result")
    return result
    } catch (e: Exception) {
        println("Retrieve data [getArtistSearch] failed: ${e.message}")
        return initArtistBio()
    }
}

suspend fun getSimilarData(searchQuery: String): List<ArtistBio> {
    try{
    val result = appAPI.getSimilar(searchQuery)
    return result
    } catch (e: Exception) {
        println("Retrieve data [getSimilarData] failed: ${e.message}")
        return emptyList()
    }
}

suspend fun getArtworks(searchQuery: String): List<Artwork> {
    try{
    val result = appAPI.getArtworks(searchQuery)
    val artworksResult = result._embedded.artworks
    return artworksResult
    } catch (e: Exception) {
        println("Retrieve data [getArtworks] failed: ${e.message}")
        return emptyList()
    }
}

suspend fun getCategories(searchQuery: String): List<Gene> {
    try{
    val result = appAPI.getCategories(searchQuery)
    val catResult = result._embedded.genes
    return catResult
    } catch (e: Exception) {
        println("Retrieve data [getCategories] failed: ${e.message}")
        return emptyList()
    }
}

suspend fun postLogin(loginQuery: MutableMap<String, String>): LoginRespond{
    try{
    val result = appAPI.loginUser(loginQuery)
    return result
    } catch (e: Exception) {
        println("Sending-Retrieve data [postLogin] failed: ${e.message}")
        var errorRespond = initLoginRespond()
        errorRespond.acknowledged = "error"
        return errorRespond
    }
}

suspend fun postRegister(regQuery: MutableMap<String, String>): LoginRespond {
    try{
    val result = appAPI.registerUser(regQuery)
    return result
    } catch (e: Exception) {
        println("Sending-Retrieve data [postRegister] failed: ${e.message}")
        var errorRespond = initLoginRespond()
        errorRespond.acknowledged = "error"
        return errorRespond
    }
}

suspend fun postFavorite(favQuery :MutableMap<String, String>): List<Favorite> {
    try{
    val result = appAPI.fetchFavorite(favQuery)
    return result
    } catch (e: Exception) {
        println("Sending-Retrieve data [postFavorite] failed: ${e.message}")
        return emptyList()
    }
}

suspend fun fetchUserEndPoint(): LoginRespond {
    try{
    val result = appAPI.fetchUserApi()
    return result
    } catch (e: Exception) {
        println("Fetch data [fetchUserEndPoint] failed: ${e.message}")
        return initLoginRespond()
    }
}

suspend fun postLogOut(): String {
    try{
    val result = appAPI.logoutUser()
    println("Server response -> $result")
    RetrofitClient.removeCookie()
    return result["acknowledged"].toString()
    } catch (e: Exception) {
        println("Sending-Retrieve data [postLogOut] failed: ${e.message}")
        return "false"
    }
}

suspend fun postDeleteAccount(): String {
    try{
    val result = appAPI.deleteUser()
    RetrofitClient.removeCookie()
    return result["acknowledged"].toString()
    } catch (e: Exception) {
        println("Sending-Retrieve data [postDeleteAccount] failed: ${e.message}")
        return "false"
    }
}
