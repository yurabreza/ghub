package co.me.ghub.data.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("total_count") val count: Int,
    @SerializedName("items") val repos: List<Repository>
)

