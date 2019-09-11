package co.me.ghub.presentation.login

import co.me.ghub.presentation.base.MvpView

interface LoginView : MvpView {
    fun loginSuccess()

    fun loginFailed()
}