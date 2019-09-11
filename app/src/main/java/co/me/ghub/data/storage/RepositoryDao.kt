package co.me.ghub.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.me.ghub.data.model.Repository

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM repository")
    fun getRepositories(): List<Repository>

    @Query("SELECT * FROM repository WHERE fullName LIKE :query AND description LIKE :query ORDER BY stars DESC ")
    suspend fun getRepositoriesByNameSortedByStars(query: String): List<Repository>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRepositories(repositories: List<Repository>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRepositories(repositories: List<Repository>)

    @Delete
    suspend fun delete(repository: Repository)
}