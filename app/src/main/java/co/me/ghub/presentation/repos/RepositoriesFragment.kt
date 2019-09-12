package co.me.ghub.presentation.repos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.me.ghub.R.drawable
import co.me.ghub.R.string
import co.me.ghub.data.model.Repository
import co.me.ghub.presentation.base.MvpFragment
import co.me.ghub.presentation.repos.adapter.DragHelper
import co.me.ghub.presentation.repos.adapter.RepositoriesAdapter
import co.me.ghub.presentation.util.RecyclerBottomScrolledListener
import co.me.ghub.presentation.util.RecyclerBottomScrolledListener.BottomScrolledListener
import co.me.ghub.presentation.util.ext.hideKeyboard
import co.me.ghub.presentation.util.isOnline
import org.jetbrains.anko.dip
import org.jetbrains.anko.editText
import org.jetbrains.anko.find
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.progressBar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.view
import org.jetbrains.anko.wrapContent
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class RepositoriesFragment : MvpFragment(), RepositoriesView, BottomScrolledListener {

    override val presenter: RepositoriesPresenter by currentScope.inject { parametersOf(this) }

    private val progressBar by lazy { view?.find<ProgressBar>(Id.progressBar) }
    private val searchText by lazy { view?.find<EditText>(Id.searchText) }

    private val onRepositoryClickListener = { url: String ->
        context!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
    private val onRepositoryDeleteListener = { repo: Repository ->
        presenter.deleteRepository(repo)
    }

    private val reposAdapter = RepositoriesAdapter(onRepositoryClickListener, onRepositoryDeleteListener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            frameLayout {
                lparams(matchParent, matchParent)
                linearLayout {
                    editText {
                        id = Id.searchText
                        setText(context.getString(string.text_kotlin))
                        maxLines = 1
                    }.lparams(matchParent, wrapContent) {
                        margin = dip(16)
                        weight = 1f
                    }
                    view {
                        background = ContextCompat.getDrawable(context, drawable.ic_search)
                        setOnClickListener(::onSearchClicked)
                    }.lparams(
                        dip(40), dip(40)
                    ) {
                        margin = dip(16)
                    }
                }
                recyclerView {
                    id = Id.recyclerView
                    padding = dip(8)
                    lparams(matchParent, matchParent)
                    layoutManager = LinearLayoutManager(context)
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(48)
                }
                progressBar {
                    padding = dip(8)
                    background = context!!.resources.getDrawable(drawable.ic_cross, context.theme)
                    id = Id.progressBar
                    visibility = VISIBLE
                    setOnClickListener(::cancelSearch)
                }.lparams {
                    gravity = CENTER
                }
            }
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        presenter.searchNewRequest(searchText!!.text.toString())
    }

    private fun initRecyclerView() {
        view?.find<RecyclerView>(Id.recyclerView)?.apply {
            adapter = reposAdapter
            val listener =
                RecyclerBottomScrolledListener(layoutManager as LinearLayoutManager, this@RepositoriesFragment)
            addOnScrollListener(listener)
            val dragHelper = DragHelper(reposAdapter)
            val touchHelper = ItemTouchHelper(dragHelper)
            reposAdapter.touchHelper = touchHelper
            touchHelper.attachToRecyclerView(this)
        }
    }

    private fun cancelSearch(v: View) {
        presenter.cancelSearch()
        progressBar?.isVisible = false
    }

    private fun onSearchClicked(v: View) {
        progressBar?.isVisible = false
        v.hideKeyboard()
        reposAdapter.items.clear()
        reposAdapter.notifyDataSetChanged()
        progressBar?.isVisible = true
        presenter.searchNewRequest(searchText!!.text.toString())
    }

    override fun onScrolled() {
        if (progressBar?.isVisible == false && isOnline()) {
            progressBar?.isVisible = true
            presenter.requestNextPage(searchText!!.text.toString())
        }
    }

    override fun updateRepos(repos: List<Repository>) {
        reposAdapter.items.addAll(repos)
        reposAdapter.notifyItemRangeChanged(reposAdapter.items.size, reposAdapter.items.size + repos.size)
        progressBar?.isVisible = false
    }

    override fun onError(message: String?) {
        super.onError(message)
        progressBar?.isVisible = false
    }

    object Id {
        @IdRes val searchText = View.generateViewId()
        @IdRes val progressBar = View.generateViewId()
        @IdRes val recyclerView = View.generateViewId()
    }
}
