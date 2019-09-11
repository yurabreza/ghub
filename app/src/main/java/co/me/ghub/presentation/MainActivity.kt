package co.me.ghub.presentation

import android.R.anim
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.me.ghub.data.storage.prefs.IPreferencesStorage
import co.me.ghub.presentation.MainActivity.Id.root
import co.me.ghub.presentation.login.LoginFragment
import co.me.ghub.presentation.login.LoginFragment.OnLoginListener
import co.me.ghub.presentation.repos.RepositoriesFragment
import org.jetbrains.anko.frameLayout
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() ,OnLoginListener{

    private val prefs :IPreferencesStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout { id = root }
        if (prefs.getGithubAuthHeader().isEmpty()) {
            replaceFragment(LoginFragment())
        } else {
            replaceFragment(RepositoriesFragment())
        }
    }

    override fun login() {
        replaceFragment(RepositoriesFragment())
    }

    private fun replaceFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(root, frag, frag::class.java.simpleName)
            .setCustomAnimations(anim.fade_in, anim.fade_out)
            .commitAllowingStateLoss()
    }

    object Id {
        @IdRes val root = View.generateViewId()
    }
}
