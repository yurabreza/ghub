package co.me.ghub.presentation.base

import androidx.fragment.app.Fragment
import co.me.ghub.R
import org.jetbrains.anko.support.v4.toast

abstract class MvpFragment : Fragment(), MvpView {
    abstract val presenter: MvpPresenter

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbind()
    }

    override fun onError(message: String?) {
        toast(message ?: getString(R.string.error_undefined))
    }
}