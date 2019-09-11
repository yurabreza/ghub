package co.me.ghub.presentation.base

abstract class MvpPresenter(view: MvpView) {
    abstract fun unbind()
}