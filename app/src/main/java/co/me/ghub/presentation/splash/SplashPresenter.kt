package co.me.ghub.presentation.splash

import co.me.ghub.data.storage.prefs.IPreferencesStorage
import co.me.ghub.presentation.base.MvpPresenter

class SplashPresenter(
    private val view: SplashView,
    private val prefs: IPreferencesStorage
) : MvpPresenter(view) {

    fun launchRepoScreenIfAuthorizedOrLoginScreenOtherwise() {
        if (prefs.isUserTokenStored()) {
            view.gotoRepositoriesScreen()
        } else {
            view.goToLoginScreen()
        }
    }
}