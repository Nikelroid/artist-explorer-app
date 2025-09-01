package com.nikelroid.artist_pedia

import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nikelroid.artist_pedia.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.String
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf

data class UserInfo(
    val favorites: MutableList<Favorite> = mutableListOf<Favorite>(),
    val favoriteIds: MutableList<String> = mutableListOf<String>(),
    val avatar: String = "",
    val auth: String = "",
    val lastSearch: String = ""

)

class UserObject: ViewModel() {
    var user by mutableStateOf(UserInfo())

        private set

    fun lastSearchSetter(lastSearch: String){
        user = user.copy(lastSearch = lastSearch)
    }

    fun userSetter(userdata: LoginRespond){
        user = user.copy(avatar = userdata.avatar,
            auth = userdata.id)
        if (userdata.favorites==null){ user = user.copy(favorites = mutableListOf<Favorite>())
        }else{ user = user.copy(favorites = userdata.favorites.toMutableList().asReversed()) }
        indexFaves()
    }

    fun clear(){
        user = user.copy(avatar = "", auth = "",
            favorites = mutableListOf<Favorite>(),
            favoriteIds = mutableListOf<String>())
    }

    fun searchFaveId(artistId: String): Boolean{
        return (artistId in user.favoriteIds)
    }

    fun indexFaves(){
        var favIn : MutableList<String> = mutableListOf<String>()
        for (fave in user.favorites){
            favIn.add(fave.artistId)
        }
        user = user.copy(favoriteIds = favIn)

    }

    fun setFaves (favorites: MutableList<Favorite>){
        user = user.copy(favorites = favorites)
        indexFaves()
    }

    suspend fun addToFaves (artistId:String){
        val map = mutableMapOf("action" to "add", "id" to artistId)
        val updatedFav = postFavorite(map)
        setFaves(updatedFav.toMutableList())
    }

    suspend fun removeFromFaves (artistId:String){
        val map = mutableMapOf("action" to "remove", "id" to artistId)
        val updatedFav = postFavorite(map)
        setFaves(updatedFav.toMutableList())
    }

    suspend fun toggleFave (scope: CoroutineScope, artistId:String): Boolean{
            if (searchFaveId(artistId)) {
                removeFromFaves(artistId)
                return false
            } else {
                addToFaves(artistId)
                return true
            }
    }

}

suspend fun fetchUser(userData: UserObject){
    val loginRespond = fetchUserEndPoint()
    if(loginRespond.id==null){
        userData.userSetter(initLoginRespond())
    }else{
        userData.userSetter(loginRespond)
    }
}
fun UserObject.clearWithDelay(delayTimeMs: Long = 1500, scope: CoroutineScope) {
    scope.launch {
        delay(delayTimeMs)
        clear()
    }
}