package co.me.ghub.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repository")
data class Repository(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @SerializedName("full_name") var fullName: String = "",
    @Ignore var owner: Owner = Owner(""),
    var description: String = "",
    @SerializedName("html_url") var url: String = "",
    @SerializedName("stargazers_count") var stars: Int = 0,
    @SerializedName("forks_count") var forks: Int = 0,
    var language: String? = ""
) {
    fun copy() = Repository(id, fullName, owner, description, url, stars, forks, language)
}