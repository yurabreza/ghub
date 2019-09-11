package co.me.ghub.presentation.repos

import android.util.Log
import co.me.ghub.data.model.Repository
import co.me.ghub.data.storage.repository.IRepositoriesRepository
import co.me.ghub.presentation.base.MvpPresenter
import co.me.ghub.presentation.util.isOnline
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class RepositoriesPresenter(
    private val view: RepositoriesView,
    private val mainDispatcher: CoroutineDispatcher,
    private val repositoriesRepository: IRepositoriesRepository
) : MvpPresenter(view) {

    private var supervisor = SupervisorJob()
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            GlobalScope.launch(mainDispatcher) {
                view.onError(exception.message)
            }
        }
    private var firstCoroutinePageNumber = 1
    private var secondCoroutinePageNumber = 2

    fun requestNextPage(keyword: String) {
        incrementRequestPageNumbers()
        getRepoData(keyword)
    }

    fun searchNewRequest(keyword: String) {
        if (isOnline()) {
            resetRequestPageNumbers()
            getRepoData(keyword)
        } else {
            requestLocalData(keyword)
        }
    }

    private fun requestLocalData(keyword: String) {
        GlobalScope.launch(mainDispatcher + supervisor + coroutineExceptionHandler) {
            view.updateRepos(repositoriesRepository.getLocalData(keyword))
        }
    }

    fun deleteRepository(repo: Repository) {
        GlobalScope.launch(supervisor + coroutineExceptionHandler) {
            repositoriesRepository.deleteRepository(repo)
        }
    }

    private fun incrementRequestPageNumbers() {
        firstCoroutinePageNumber += 2
        secondCoroutinePageNumber += 2
    }

    private fun resetRequestPageNumbers() {
        firstCoroutinePageNumber = 1
        secondCoroutinePageNumber = 2
    }

    private fun getRepoData(keyword: String) =
        GlobalScope.launch(mainDispatcher + supervisor + coroutineExceptionHandler) {
            val list = getSearchResultsAsync(this, keyword, firstCoroutinePageNumber)
            val list2 = getSearchResultsAsync(this, keyword, secondCoroutinePageNumber)

            val repos = list.await() + list2.await()
            view.updateRepos(repos)
        }

    private fun getSearchResultsAsync(
        coroutineScope: CoroutineScope,
        keyword: String,
        page: Int
    ): Deferred<List<Repository>> = coroutineScope.async(Dispatchers.IO) {
        logThreadData(page, true)
        val res = repositoriesRepository.getSearchResultsAsync(keyword, page)
        logThreadData(page, false)
        res
    }

    private fun logThreadData(page: Int, inProgress: Boolean) {
        Log.d("MyTag", "PageNum $page inProgress = $inProgress, thread = ${Thread.currentThread().name}")
    }

    fun cancelSearch() {
        supervisor.cancelChildren()
    }

    override fun unbind() {
        supervisor.cancel()
    }
}