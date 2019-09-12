package co.me.ghub.presentation.splash

import android.content.Context
import androidx.navigation.fragment.findNavController
import co.me.ghub.R
import co.me.ghub.presentation.base.MvpFragment
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class SplashFragment : MvpFragment(), SplashView {
    override val presenter: SplashPresenter by currentScope.inject {
        parametersOf(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter.launchRepoScreenIfAuthorizedOrLoginScreenOtherwise()
    }

    override fun goToLoginScreen() {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }

    override fun gotoRepositoriesScreen() {
        findNavController().navigate(R.id.action_splashFragment_to_repositoriesFragment)
    }
}