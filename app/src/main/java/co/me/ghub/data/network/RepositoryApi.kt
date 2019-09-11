package co.me.ghub.data.network

import co.me.ghub.data.ITEMS_PER_PAGE
import co.me.ghub.data.model.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RepositoryApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Header("Authorization") authHeader: String,
        @Query("q") keywords: String,
        @Query("page") pageNumber: Int,
        @Query("sort") sortBy: String = "stars",
        @Query("per_page") perPage: Int = ITEMS_PER_PAGE
    ): Response<SearchResult>
}