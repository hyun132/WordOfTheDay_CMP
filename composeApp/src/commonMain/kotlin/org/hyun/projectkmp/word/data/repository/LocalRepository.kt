package org.hyun.projectkmp.word.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.string

class LocalRepository(
    private val settings: Settings
) {
    fun saveDifficulty(difficulty: String) {
        settings.string("difficulty", difficulty)
    }

    fun getDifficulty(): String? {
        return settings.getStringOrNull("difficulty")
    }

    fun saveTopic(topic: String) {
        settings.putString("subject", topic)
    }

    fun getSubject(): String? {
        return settings.getStringOrNull("subject")
    }

    fun saveName(name: String) {
        settings.putString("name", name)
    }

    fun getName(): String? {
        return settings.getStringOrNull("name")
    }

    fun saveToken(token: String) {
        settings.putString("token", token)
    }

    fun getToken(token: String): String? {
        return settings.getStringOrNull("token")
    }
}