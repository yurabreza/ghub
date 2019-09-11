package co.me.ghub.presentation.base

import androidx.fragment.app.Fragment

abstract class MvpFragment : Fragment() {
    abstract val presenter: MvpPresenter


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbind()
    }
}