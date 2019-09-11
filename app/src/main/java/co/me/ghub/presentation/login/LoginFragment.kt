package co.me.ghub.presentation.login

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import co.me.ghub.BuildConfig.BASE_LOGIN_URL
import co.me.ghub.BuildConfig.CLIENT_ID
import co.me.ghub.BuildConfig.CLIENT_REDIRECT
import co.me.ghub.BuildConfig.CLIENT_SECRET
import co.me.ghub.R
import co.me.ghub.R.string
import co.me.ghub.presentation.base.MvpFragment
import co.me.ghub.presentation.login.LoginFragment.Id.loginButton
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import org.jetbrains.anko.padding
import org.jetbrains.anko.progressBar
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class LoginFragment : MvpFragment(), LoginView {

    override val presenter: LoginPresenter by currentScope.inject { parametersOf(this) }

    private val progressBar by lazy { view?.find<ProgressBar>(Id.progressBar) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            verticalLayout {
                gravity = Gravity.CENTER
                padding = dip(32)

                textView {
                    text = context.getString(string.title_login)
                    setTypeface(typeface, Typeface.BOLD)
                    gravity = Gravity.CENTER
                    padding = dip(16)
                }
                button {
                    id = loginButton
                    text = context.getString(string.text_login)

                }
                progressBar {
                    padding = dip(8)
                    background = context!!.resources.getDrawable(R.drawable.bg_progress, context.theme)
                    id = Id.progressBar
                    visibility = View.GONE
                }.lparams {
                    gravity = Gravity.CENTER
                }
            }
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.find<Button>(Id.loginButton)?.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("$BASE_LOGIN_URL?client_id=$CLIENT_ID&redirect_uri=$CLIENT_REDIRECT")
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.intent?.data?.let {
            if (it.toString().startsWith(CLIENT_REDIRECT)) {
                val code = it.getQueryParameter("code")!!
                progressBar?.isVisible = true
                presenter.getAccessToken(CLIENT_ID, CLIENT_SECRET, code)
            }
        }
    }

    override fun loginSuccess() {
        progressBar?.isVisible = false
        (context as OnLoginListener).login()
    }

    override fun loginFailed() {
        progressBar?.isVisible = false
        toast(getString(string.text_login_failed))
    }

    interface OnLoginListener {
        fun login()
    }

    object Id {
        @IdRes val loginButton = View.generateViewId()
        @IdRes val progressBar = View.generateViewId()
    }
}
