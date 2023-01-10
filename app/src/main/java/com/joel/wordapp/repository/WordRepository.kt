package com.joel.wordapp.repository

import com.joel.wordapp.data.WordDao
import com.joel.wordapp.data.models.Word

class WordRepository(private val wordDao: WordDao) {
    private var counter = 0L
    private var wordsMap: MutableMap<Long, Word> = mutableMapOf()

//            "Captain",
//            "A naval officer of high rank, above a commander, or a military officer of middle rank, above a lieutenant.",
//            "Commander",
//            "A captain is the person in charge of a ship or aircraft."

    suspend fun getWords(str: String, status: Boolean = false): List<Word> {
        return wordDao.getWords().filter {
            Regex(
                str,
                RegexOption.IGNORE_CASE
            ).containsMatchIn(it.title) && it.status == status
        }.toList()
    }

    suspend fun addWord(word: Word) {
        wordDao.insert(word)
    }

    suspend fun getWordById(id: Long): Word? {
        return wordDao.getWordById(id)
    }

    suspend fun updateWord(id: Long, word: Word) {
        wordDao.insert(word.copy(id = id))
    }

    suspend fun deleteWord(id: Long) {
        wordDao.delete(id)
    }

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