package co.me.ghub.presentation.splash

import co.me.ghub.presentation.base.MvpView

interface SplashView:MvpView{
    fun goToLoginScreen()
    fun gotoRepositoriesScreen()
}