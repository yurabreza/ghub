package co.me.ghub.presentation.repos

import co.me.ghub.data.model.Repository
import co.me.ghub.presentation.base.MvpView

interface RepositoriesView : MvpView {
    fun updateRepos(repos: List<Repository>)
}