package org.hyun.projectkmp.word.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
class Word(
    val word: String,
    val meaning: String,
    @PrimaryKey
    val learnedAt: String
)
