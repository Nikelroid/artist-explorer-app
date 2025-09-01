package com.nikelroid.artist_pedia

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ArtistPediaApi {


    @GET("api/artist/search/{artistName}")
    suspend fun searchArtist(@Path("artistName") artistName: String): List<ArtistSearchResult>

    @GET("api/artist/similar/{artistId}")
    suspend fun getSimilar(@Path("artistId") artistId: String): List<ArtistBio>

    @GET("api/artist/bio/{artistID}")
    suspend fun getArtist(@Path("artistID") artistID: String): ArtistBio

    @GET("api/artist/artwork/artist_id={artistID}")
    suspend fun getArtworks(@Path("artistID") artistID: String): ArtworksResponse

    @GET("api/artwork/categories/{artworkID}")
    suspend fun getCategories(@Path("artworkID") artworkID: String): GeneResponse

    @GET("api/me")
    suspend fun fetchUserApi(): LoginRespond

    @POST("api/user/login")
    suspend fun loginUser(@Body loginRequest: MutableMap<String, String>): LoginRespond

    @POST("api/user/register")
    suspend fun registerUser(@Body registerRequest: MutableMap<String, String>): LoginRespond

    @POST("api/user/favorites")
    suspend fun fetchFavorite(@Body registerRequest: MutableMap<String, String>): List<Favorite>

    @POST("api/user/logout")
    suspend fun logoutUser(): Map<String, String>

    @POST("api/user/delete")
    suspend fun deleteUser(): Map<String, String>


}