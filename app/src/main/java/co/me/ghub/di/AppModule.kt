package co.me.ghub.di

import android.os.Handler
import android.os.Looper
import co.me.ghub.presentation.login.LoginFragment
import co.me.ghub.presentation.login.LoginPresenter
import co.me.ghub.presentation.login.LoginView
import co.me.ghub.presentation.repos.RepositoriesFragment
import co.me.ghub.presentation.repos.RepositoriesPresenter
import co.me.ghub.presentation.repos.RepositoriesView
import co.me.ghub.presentation.splash.SplashFragment
import co.me.ghub.presentation.splash.SplashPresenter
import co.me.ghub.presentation.splash.SplashView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.android.asCoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {
    single { getMainDispatcher() }
    scope(named<SplashFragment>()) { scoped { (view: SplashView) -> SplashPresenter(view, get()) } }
    scope(named<RepositoriesFragment>()) {
        scoped { (view: RepositoriesView) -> RepositoriesPresenter(view, get(), get()) }
    }
    scope(named<LoginFragment>()) {
        scoped { (view: LoginView) -> LoginPresenter(view, get(), get(), get()) }
    }
}

private fun getMainDispatcher() = Handler(Looper.getMainLooper()).asCoroutineDispatcher() as CoroutineDispatcher

