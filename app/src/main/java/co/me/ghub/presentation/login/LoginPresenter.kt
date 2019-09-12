package co.me.ghub.presentation.login

import android.annotation.SuppressLint
import co.me.ghub.data.network.LoginApi
import co.me.ghub.data.storage.prefs.IPreferencesStorage
import co.me.ghub.presentation.base.MvpPresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginPresenter(
    private val view: LoginView,
    private val loginApi: LoginApi,
    private val mainDispatcher: CoroutineDispatcher,
    private val prefs: IPreferencesStorage
) : MvpPresenter(view) {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        view.onError(exception.message)
    }

    @SuppressLint("ApplySharedPref")
    fun getAccessToken(clientId: String, clientSecret: String, code: String) {
        GlobalScope.launch(mainDispatcher + supervisor + coroutineExceptionHandler) {
            val result = loginApi.getAccessToken(clientId, clientSecret, code)
            if (result.isSuccessful) {
                prefs.saveGithubAuthHeader(result.body()!!.accessToken)
                view.loginSuccess()
            } else {
                view.loginFailed()
            }
        }
    }
}