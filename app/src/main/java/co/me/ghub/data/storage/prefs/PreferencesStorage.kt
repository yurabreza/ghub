package co.me.ghub.data.storage.prefs

import android.content.Context
import co.me.ghub.data.KEY_GITHUB_ACCESS_TOKEN
import org.jetbrains.anko.defaultSharedPreferences

class PreferencesStorage(
    context: Context
) : IPreferencesStorage {

    private val prefs = context.defaultSharedPreferences

    override fun saveGithubAuthHeader(token: String) {
        prefs.edit()
            .putString(KEY_GITHUB_ACCESS_TOKEN, token)
            .apply()
    }

    override fun getGithubAuthHeader(): String {
        return "token ${prefs.getString(KEY_GITHUB_ACCESS_TOKEN, "")}"
    }
}