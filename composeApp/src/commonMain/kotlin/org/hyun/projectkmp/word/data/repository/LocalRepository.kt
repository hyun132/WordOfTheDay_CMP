package org.hyun.projectkmp.word.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.string

class LocalRepository(
    private val settings: Settings
) {
    fun saveDifficulty(token: String) {
        settings.string("difficulty", token)
    }

    fun getDifficulty(): String? {
        return settings.getStringOrNull("difficulty")
    }

    fun saveSubject(subject: String) {
        settings.putString("subject", subject)
    }

    fun getSubject(): String? {
        return settings.getStringOrNull("subject")
    }

    fun saveToken(token: String) {
        settings.putString("token", token)
    }

    fun getToken(token: String): String? {
        return settings.getStringOrNull("token")
    }
}