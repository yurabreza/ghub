package co.me.ghub.presentation.login

import android.annotation.SuppressLint
import co.me.ghub.data.network.LoginApi
import co.me.ghub.data.storage.prefs.IPreferencesStorage
import co.me.ghub.presentation.base.MvpPresenter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginPresenter(
    private val view: LoginView,
    private val loginApi: LoginApi,
    private val prefs: IPreferencesStorage
) : MvpPresenter(view) {

    private val supervisor = SupervisorJob()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }

    @SuppressLint("ApplySharedPref")
    fun getAccessToken(clientId: String, clientSecret: String, code: String) {
        GlobalScope.launch(supervisor + coroutineExceptionHandler) {
            val result = loginApi.getAccessToken(clientId, clientSecret, code)

            if (result.isSuccessful) {
                prefs.saveGithubAuthHeader(result.body()!!.accessToken)
                //todo fix transition to next screen
                view.loginSuccess()
            } else {
                view.loginFailed()
            }
        }
    }

    override fun unbind() {
        supervisor.cancel()
    }
}