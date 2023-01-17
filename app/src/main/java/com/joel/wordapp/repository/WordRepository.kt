package com.joel.wordapp.repository

import com.joel.wordapp.data.WordDao
import com.joel.wordapp.data.models.Word

// WordRepository class that sends function requests to the DAO queries
class WordRepository(private val wordDao: WordDao) {
//    private var counter = 0L
//    private var wordsMap: MutableMap<Long, Word> = mutableMapOf()
//            "Captain",
//            "A naval officer of high rank, above a commander, or a military officer of middle rank, above a lieutenant.",
//            "Commander",
//            "A captain is the person in charge of a ship or aircraft."

    // function send to DAO to query all the data from RD and filters the words based on its status property
    suspend fun getWords(str: String, status: Boolean = false): List<Word> {
        return wordDao.getWords().filter {
            Regex(
                str,
                RegexOption.IGNORE_CASE
            ).containsMatchIn(it.title) && it.status == status
        }.toList()
    }

    // function send to DAO to add new data to the RD
    suspend fun addWord(word: Word) {
        wordDao.insert(word)
    }

    // function sent to DAO to get a single data from the RD
    suspend fun getWordById(id: Long): Word? {
        return wordDao.getWordById(id)
    }

    // function sent to DAO to update a single data in the RD
    suspend fun updateWord(id: Long, word: Word) {
        wordDao.insert(word.copy(id = id))
    }

    // function sent to DAO to DELETE a single data from RD
    suspend fun deleteWord(id: Long) {
        wordDao.delete(id)
    }

    // function sent to DAO to change the status property of a single data in the RD
    suspend fun changeStatus(id: Long) {
        wordDao.updateStatusById(id, true)
    }

//    companion object {
//        private var wordRepository: WordRepository? = null
//
//        fun getInstance(): WordRepository {
//            if (wordRepository == null) {
//                wordRepository = WordRepository()
//            }
//            return wordRepository!!
//        }
//    }
}