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

    override fun getGithubAuthHeaderToken(): String {
        return prefs.getString(KEY_GITHUB_ACCESS_TOKEN, "")!!
    }

    override fun isUserTokenStored(): Boolean {
        return prefs.getString(KEY_GITHUB_ACCESS_TOKEN, "")!!.isNotEmpty()
    }
}