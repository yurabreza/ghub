package co.me.ghub.di

import co.me.ghub.presentation.login.LoginFragment
import co.me.ghub.presentation.login.LoginPresenter
import co.me.ghub.presentation.login.LoginView
import co.me.ghub.presentation.repos.RepositoriesFragment
import co.me.ghub.presentation.repos.RepositoriesPresenter
import co.me.ghub.presentation.repos.RepositoriesView
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {
    scope(named<RepositoriesFragment>()) {
        scoped { (view: RepositoriesView, mainDispatcher: CoroutineDispatcher) ->
            RepositoriesPresenter(view, mainDispatcher, get())
        }
    }
    scope(named<LoginFragment>()) {
        scoped { (view: LoginView) -> LoginPresenter(view, get(), get()) }
    }
}

