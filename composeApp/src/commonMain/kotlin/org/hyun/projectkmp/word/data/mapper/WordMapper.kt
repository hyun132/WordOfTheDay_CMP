package org.hyun.projectkmp.word.data.mapper

import org.hyun.projectkmp.word.data.dto.SentenceDto
import org.hyun.projectkmp.word.data.dto.WordDto
import org.hyun.projectkmp.word.domain.Sentence
import org.hyun.projectkmp.word.domain.Word

fun WordDto.toWord(): Word {
    return Word(
        word = word ?: ""
    )
}

fun SentenceDto.toSentence(): List<Sentence> {
    return sentence?.map {
        Sentence(
            sentence = it ?: ""
        )
    } ?: emptyList()
}