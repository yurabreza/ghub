package co.me.ghub.data.storage.repository

import co.me.ghub.data.model.Repository
import co.me.ghub.data.network.RepositoryApi
import co.me.ghub.data.storage.RepositoriesDatabase
import co.me.ghub.presentation.util.ext.toSqlLikeTemplate

class RepositoriesRepository(
    private val repositoryApi: RepositoryApi,
    private val repositoriesDatabase: RepositoriesDatabase
) : IRepositoriesRepository {

    override suspend fun getSearchResultsAsync(keyword: String, page: Int): List<Repository> {
        val response = repositoryApi.searchRepositories(keyword, page)
        if (response.isSuccessful) {
            response.body()!!.repos.apply {
                repositoriesDatabase.repositoryDao().insertRepositories(this)
                return this
            }
        } else throw IllegalStateException("${response.code()} ${response.message()}")
    }

    override suspend fun getLocalData(keyword: String): List<Repository> =
        repositoriesDatabase.repositoryDao().getRepositoriesByNameSortedByStars(keyword.toSqlLikeTemplate())

    override suspend fun deleteRepository(repo: Repository) {
        repositoriesDatabase.repositoryDao().delete(repo)
    }
}
