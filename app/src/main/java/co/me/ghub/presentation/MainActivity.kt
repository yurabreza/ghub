package co.me.ghub.presentation

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import co.me.ghub.R
import co.me.ghub.presentation.MainActivity.Id.root
import org.jetbrains.anko.frameLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout { id = root }
        setupNavHost(NavHostFragment.create(R.navigation.nav_graph))
    }

    private fun setupNavHost(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(root, frag, frag::class.java.simpleName)
            .setPrimaryNavigationFragment(frag)
            .commitAllowingStateLoss()
    }

    object Id {
        @IdRes val root = View.generateViewId()
    }
}
