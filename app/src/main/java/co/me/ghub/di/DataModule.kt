package co.me.ghub.di

import android.content.Context
import androidx.room.Room
import co.me.ghub.data.DATABASE_NAME
import co.me.ghub.data.network.RepositoryApi
import co.me.ghub.data.storage.RepositoriesDatabase
import co.me.ghub.data.storage.prefs.IPreferencesStorage
import co.me.ghub.data.storage.prefs.PreferencesStorage
import co.me.ghub.data.storage.repository.IRepositoriesRepository
import co.me.ghub.data.storage.repository.RepositoriesRepository
import org.koin.dsl.module

val dataModule = module {
    single { getPrefsStorage(get()) }
    single { getRepositoriesDataBase(get()) }
    single { getRepositoriesRepository(get(), get()) }
}

fun getRepositoriesDataBase(applicationContext: Context): RepositoriesDatabase =
    Room.databaseBuilder(applicationContext, RepositoriesDatabase::class.java, DATABASE_NAME).build()

fun getPrefsStorage(context: Context): IPreferencesStorage {
    return PreferencesStorage(context)
}

fun getRepositoriesRepository(
    repositoryApi: RepositoryApi,
    repositoriesDatabase: RepositoriesDatabase
): IRepositoriesRepository {
    return RepositoriesRepository(repositoryApi, repositoriesDatabase)
}
