package co.fanavari.androidfanavari40205.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import co.fanavari.androidfanavari40205.api.UnsplashApi
import co.fanavari.myapplication20.repository.UnsplashPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
    private val unsplashApi: UnsplashApi
){
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 40,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
              UnsplashPagingSource(
                  unsplashApi,
                  query
              )
            }
        ).liveData
}