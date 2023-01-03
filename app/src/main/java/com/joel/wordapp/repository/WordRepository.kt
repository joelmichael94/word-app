package com.joel.wordapp.repository

import com.joel.wordapp.models.Word

class WordRepository {
    private var counter = 0L
    private val wordsMap: MutableMap<Long, Word> = mutableMapOf(
        0L to Word(
            0L,
            "Captain",
            "A naval officer of high rank, above a commander, or a military officer of middle rank, above a lieutenant.",
            "Commander",
            "A captain is the person in charge of a ship or aircraft."
        )
    )

    fun getWords(str: String, status: Boolean = false): List<Word> {
        return wordsMap.filter { (key, value) ->
            Regex(
                str,
                RegexOption.IGNORE_CASE
            ).containsMatchIn(value.title) && value.status == status
        }.values.toList()
    }

    fun addWord(word: Word): Word? {
        wordsMap[++counter] = word.copy(id = counter)
        return wordsMap[counter]
    }

    fun getWordById(id: Long): Word? {
        return wordsMap[id]
    }

    fun updateWord(id: Long, word: Word): Word? {
        wordsMap[id] = word
        return wordsMap[id]
    }

    fun deleteWord(id: Long) {
        wordsMap.remove(id)
    }

    fun changeStatus(id: Long): Word? {
        wordsMap[id]?.status = !wordsMap[id]?.status!!
        return wordsMap[id]
    }

    companion object {
        private var wordRepository: WordRepository? = null

        fun getInstance(): WordRepository {
            if (wordRepository == null) {
                wordRepository = WordRepository()
            }
            return wordRepository!!
        }
    }
}