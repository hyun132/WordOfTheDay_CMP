package org.hyun.projectkmp.word.data.mapper

import org.hyun.projectkmp.word.data.dto.SentenceDto
import org.hyun.projectkmp.word.data.dto.LearningHistoryDto
import org.hyun.projectkmp.word.data.dto.SentencesDto
import org.hyun.projectkmp.word.data.dto.WordDto
import org.hyun.projectkmp.word.domain.LearningHistory
import org.hyun.projectkmp.word.domain.Sentence
import org.hyun.projectkmp.word.domain.Word

fun WordDto.toWord(): Word {
    return Word(
        word = word ?: ""
    )
}

fun SentencesDto.toSentences(): List<Sentence> {
    return sentences?.map {
        Sentence(
            sentence = it.sentence ?: ""
        )
    } ?: emptyList()
}

fun SentenceDto.toSentence(): Sentence {
    return Sentence(
        sentence = sentence ?: ""
    )
}

fun LearningHistoryDto.toLearningHistory():LearningHistory{
    return LearningHistory(
        learnedAt = learnedAt,
        word = word
    )
}