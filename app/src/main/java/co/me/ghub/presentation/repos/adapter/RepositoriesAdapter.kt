package co.me.ghub.presentation.repos.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import co.me.ghub.data.model.Repository
import co.me.ghub.presentation.repos.adapter.RepositoriesAdapter.ViewHolder
import co.me.ghub.presentation.repos.adapter.RepositoryViewUi.Id
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find

class RepositoriesAdapter(
    private val openUrlListener: (url: String) -> Unit,
    private val deleteListener: (repository: Repository) -> Unit
) : RecyclerView.Adapter<ViewHolder>(), ActionCompletionContract {

    val items: ArrayList<Repository> = arrayListOf()
    lateinit var touchHelper: ItemTouchHelper

    override fun getItemCount() = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RepositoryViewUi().createView(AnkoContext.create(parent.context!!, parent))
    ).apply {
        view.layoutParams =
            RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.apply {
            setOnClickListener { openUrlListener.invoke(items[position].url) }
            find<TextView>(Id.repositoryDescription).text = items[position].description
            find<TextView>(Id.title).text = items[position].fullName
            find<TextView>(Id.repositoryStars).text =
                "${holder.view.context.getString(co.me.ghub.R.string.text_stars)}${items[position].stars}"

            find<View>(Id.deleteButton).setOnClickListener { onDeleteItemClick(holder) }
            find<View>(Id.reorderButton).setOnTouchListener { v, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) touchHelper.startDrag(holder)
                false
            }
        }
    }

    private fun onDeleteItemClick(holder: ViewHolder) {
        deleteListener.invoke(items[holder.adapterPosition])
        items.remove(items[holder.adapterPosition])
        notifyItemRemoved(holder.adapterPosition)
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        val targetRepo = items[oldPosition]
        val repo = targetRepo.copy()
        items.removeAt(oldPosition)
        items.add(newPosition, repo)
        notifyItemMoved(oldPosition, newPosition)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}