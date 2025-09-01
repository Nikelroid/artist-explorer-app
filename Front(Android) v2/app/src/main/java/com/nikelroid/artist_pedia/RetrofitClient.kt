package com.nikelroid.artist_pedia

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://artist-pedia-992793754049.us-west1.run.app/"

    private var cookieJar: PersistentCookieJar? = null
    private var okHttpClient: OkHttpClient? = null

    fun initialize(context: Context) {
        if (cookieJar == null) {
            cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

            okHttpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar!!)
                .build()
        }
    }

    fun removeCookie(){
        cookieJar = null
    }

    val api: ArtistPediaApi by lazy {
        if (okHttpClient == null) {
            throw IllegalStateException("RetrofitClient must be initialized with context before use")
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient!!)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtistPediaApi::class.java)
    }
}