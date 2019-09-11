package co.me.ghub.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import co.me.ghub.data.model.Repository

@Database(entities = [Repository::class], version = 1)
abstract class RepositoriesDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao
}