package co.me.ghub.presentation.base

import kotlinx.coroutines.SupervisorJob

abstract class MvpPresenter(view: MvpView) {

    protected val supervisor = SupervisorJob()

    open fun unbind() {
        supervisor.cancel()
    }
}