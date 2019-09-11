package co.me.ghub.data.storage.repository

import co.me.ghub.data.model.Repository

interface IRepositoriesRepository {
    suspend fun getSearchResultsAsync(keyword: String, page: Int): List<Repository>

    suspend fun getLocalData(keyword: String): List<Repository>
    suspend fun deleteRepository(repo: Repository)
}