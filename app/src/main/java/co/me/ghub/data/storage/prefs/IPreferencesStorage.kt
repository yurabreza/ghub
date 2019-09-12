package co.me.ghub.data.storage.prefs

interface IPreferencesStorage {
    fun saveGithubAuthHeader(token: String)
    fun getGithubAuthHeaderToken(): String
    fun isUserTokenStored():Boolean
}