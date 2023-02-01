package com.joel.wordapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joel.wordapp.data.models.Word

// Data Access Object (DAO) to make requests/queries from the RoomDatabase(RD) for Word Entity
@Dao
interface WordDao {
    // Fetches all the data from the RD and stores it as a list of words
    @Query("SELECT * FROM word")
    suspend fun getWords(): List<Word>

    // Fetches a single data from RD where the id's match
    @Query("SELECT * FROM word WHERE id = :id")
    suspend fun getWordById(id: Long): Word?

    // Request to ADD new data into RD or UPDATE existing data in RD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: Word)

    // Request to DELETE existing data from RD where the id's match
    @Query("DELETE FROM word WHERE id = :id")
    suspend fun delete(id: Long)

    // Request to change the Entity type status for a single data in RD
    @Query("UPDATE word SET status = :status WHERE id = :id")
    suspend fun updateStatusById(id: Long, status: Boolean)
}