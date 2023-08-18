package co.fanavari.androidfanavari40205.api

import co.fanavari.androidfanavari40205.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    companion object{
        const val BASE_URL = "https://api.unsplash.com/"
//        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
        const val CLIENT_ID = "6-zNRN7ixpA3shCdJc1C8NR8HhUg8UrwYSo_5kwJGQ0"
    }

    @Headers("Accept-Version: v1","Authorization: Client-ID $CLIENT_ID")
    @GET("/search/photos")
    suspend fun searchPhoto(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashPhotoResponse

}