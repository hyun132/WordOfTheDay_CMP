package org.hyun.projectkmp.word.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
class Profile(
    @PrimaryKey
    val username: String,
    val difficulty: String,
    val topic: String,
    val createdAt:String
)
