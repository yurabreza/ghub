package co.me.ghub.data.model

import co.me.ghub.data.ITEMS_PER_PAGE
import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("total_count") val count: Int,
    @SerializedName("items") val repos: List<Repository>
) {
    fun isLastPage(pageNumber: Int): Boolean {
        return ITEMS_PER_PAGE * pageNumber >= count
    }
}

