package co.me.ghub.data.storage.repository

import co.me.ghub.data.model.Repository
import co.me.ghub.data.network.RepositoryApi
import co.me.ghub.data.storage.RepositoriesDatabase
import co.me.ghub.data.storage.prefs.IPreferencesStorage
import co.me.ghub.presentation.util.ext.toSqlLikeTempalate

class RepositoriesRepository(
    private val repositoryApi: RepositoryApi,
    private val repositoriesDatabase: RepositoriesDatabase,
    private val prefs: IPreferencesStorage
) : IRepositoriesRepository {

    override suspend fun getSearchResultsAsync(keyword: String, page: Int): List<Repository> =
        repositoryApi.searchRepositories(prefs.getGithubAuthHeader(), keyword, page).body()!!.repos.apply {
            repositoriesDatabase.repositoryDao().insertRepositories(this)
            return this
        }

    override suspend fun getLocalData(keyword: String): List<Repository> =
        repositoriesDatabase.repositoryDao().getRepositoriesByNameSortedByStars(keyword.toSqlLikeTempalate())

    override suspend fun deleteRepository(repo: Repository) {
        repositoriesDatabase.repositoryDao().delete(repo)
    }
}
