package co.me.ghub.presentation.repos.adapter

import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.view
import org.jetbrains.anko.wrapContent

class RepositoryViewUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            frameLayout {
                cardView {
                    elevation = 8f
                    verticalLayout {
                        linearLayout {
                            weightSum = 1f
                            textView {
                                gravity = Gravity.CENTER_VERTICAL
                                id = Id.title
                                textSize = 24f
                                setTypeface(typeface, Typeface.BOLD)
                                padding = dip(8)
                            }.lparams(matchParent, wrapContent) {
                                weight = 1f
                            }
                            view {
                                val outValue = TypedValue()
                                context.theme.resolveAttribute(
                                    android.R.attr.selectableItemBackground,
                                    outValue,
                                    true
                                )
                                setBackgroundResource(outValue.resourceId)
                                id = Id.deleteButton
                                foreground =
                                    ContextCompat.getDrawable(context, co.me.ghub.R.drawable.ic_delete)
                            }.lparams(dip(32), dip(32)) {
                                margin = dip(16)
                            }
                            view {
                                val outValue = TypedValue()
                                context.theme.resolveAttribute(
                                    android.R.attr.selectableItemBackground,
                                    outValue,
                                    true
                                )
                                setBackgroundResource(outValue.resourceId)
                                id = Id.reorderButton
                                foreground = ContextCompat.getDrawable(
                                    context,
                                    co.me.ghub.R.drawable.ic_reorder
                                )
                            }.lparams(dip(32), dip(32)) {
                                margin = dip(16)
                            }

                        }
                        textView {
                            id = Id.repositoryDescription
                            maxEms = 30
                            textSize = 24f
                            padding = dip(8)
                        }
                        textView {
                            id = Id.repositoryStars
                            padding = dip(8)
                        }
                    }
                }.lparams(matchParent, wrapContent) {
                    margin = dip(8)
                }
            }
        }
    }

    object Id {
        @IdRes val title = View.generateViewId()
        @IdRes val repositoryDescription = View.generateViewId()
        @IdRes val repositoryStars = View.generateViewId()
        @IdRes val deleteButton = View.generateViewId()
        @IdRes val reorderButton = View.generateViewId()
    }
}