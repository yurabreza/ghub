package co.me.ghub.presentation.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerBottomScrolledListener(
    private val layoutManager: LinearLayoutManager,
    private val listener: BottomScrolledListener
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
            listener.onScrolled()
        }
    }

    interface BottomScrolledListener {
        fun onScrolled()
    }
}
