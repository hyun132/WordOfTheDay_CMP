package org.hyun.projectkmp.word.presentation.learning

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.VoiceRecognizer
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.toUiText
import org.hyun.projectkmp.word.domain.Difficulty
import org.hyun.projectkmp.word.domain.Mode
import org.hyun.projectkmp.word.domain.model.AnswerCheckRequest
import org.hyun.projectkmp.word.domain.model.BookMarkRequestQuery
import org.hyun.projectkmp.word.domain.model.LearningCompleteRequest
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.repository.WordRepository

class LearningViewModel(
    private val repository: WordRepository,
    private val recognizer: VoiceRecognizer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var silenceTimerJob: Job? = null

    private val _state = MutableStateFlow(LeaningState())
    val state: StateFlow<LeaningState> = _state

    fun onAction(action: LearningAction) {
        when (action) {
            is LearningAction.OnBookMarkClick -> {
                if (action.isBookmarked) removeBookmark(action.sentence)
                else bookMark(action.sentence)
            }

            is LearningAction.OnScroll -> {
                _state.update {
                    it.copy(
                        progress = action.skipTo
                    )
                }
            }

            is LearningAction.OnNext -> {
                _state.update {
                    it.copy(
                        progress = if (it.sentenceItems.size - 1 > it.progress) it.progress + 1 else it.progress
                    )
                }
            }

            is LearningAction.OnTextChange -> {
                _state.update {
                    val inputs = it.sentenceItems.toMutableList().apply {
                        this[it.progress] = this[it.progress].copy(userInput = action.text)
                    }
                    it.copy(
                        sentenceItems = inputs
                    )
                }
            }

            is LearningAction.OnSubmit -> {
                val progress = state.value.progress
                val item = state.value.sentenceItems[progress]
                submitAnswer(
                    word = action.word,
                    progress = progress,
                    mode = state.value.mode,
                    origin = item.sentence,
                    userInput = item.userInput
                )
            }

            is LearningAction.OnDoneClick -> {
                println("update Word Learning state")
            }

            is LearningAction.OnDismiss -> {
                _state.update {
                    it.copy(showDialog = false)
                }
            }

            is LearningAction.OnModeClick -> {
                _state.update {
                    it.copy(mode = if (it.mode == Mode.TEXT) Mode.VOICE else Mode.TEXT)
                }
            }

            is LearningAction.OnAudioStartClick -> {
                toggleRecognition(action.word)
            }

            else -> Unit
        }
    }

    private fun bookMark(sentence: String) {
        viewModelScope.launch {
            repository.saveBookMark(
                BookMarkRequestQuery(sentence = sentence)
            )
                .onSuccess {
                    val list = state.value.sentenceItems.toMutableList()
                        .map { if (it.sentence == sentence) it.copy(isBookmarked = true) else it }
                    _state.update {
                        it.copy(isLoading = false, sentenceItems = list)
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.toUiText()
                        )
                    }
                }
        }
    }

    private fun removeBookmark(sentence: String) {
        viewModelScope.launch {
            repository.deleteBookMark(
                BookMarkRequestQuery(sentence = sentence)
            )
                .onSuccess {
                    val list = state.value.sentenceItems.toMutableList()
                        .map { if (it.sentence == sentence) it.copy(isBookmarked = false) else it }
                    _state.update {
                        it.copy(isLoading = false, sentenceItems = list)
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.toUiText()
                        )
                    }
                }
        }
    }

    fun getSentences(word: String, meaning: String, difficulty: Difficulty) {
        viewModelScope.launch {
            repository.getSentences(
                SentencesRequestQuery(
                    word = word,
                    meaning = meaning,
                    difficulty = difficulty
                )
            )
                .onSuccess { result ->
                    _state.update {
                        val list = result.map { LearningSentenceItem(sentence = it) }
                        it.copy(
                            sentenceItems = list,
                            totalSize = list.size,
                            isLoading = false
                        )
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            errorMessage = e.toUiText(),
                            isLoading = false
                        )
                    }
                    println(e.name)
                }
        }
    }

    fun submitAnswer(word: String, progress: Int, mode: Mode, origin: String, userInput: String) {
        viewModelScope.launch {
            repository.checkAnswer(
                AnswerCheckRequest(
                    origin = origin,
                    mode = mode,
                    userAnswer = userInput
                )
            )
                .onSuccess { result ->
                    var isDone = false
                    _state.update {
                        val updatedItems = it.sentenceItems.toMutableList().apply {
                            this[progress] = this[progress].copy(isCorrect = result.isCorrect)
                        }
                        isDone = updatedItems.all { it.isCorrect == true }
                        it.copy(
                            sentenceItems = updatedItems
                        )
                    }
                    if (isDone) saveLearningCompleted(word)
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            errorMessage = e.toUiText(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun saveLearningCompleted(word: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            repository.saveLearningHistory(
                LearningCompleteRequest(
                    word = word
                )
            )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            showDialog = true
                        )
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            errorMessage = e.toUiText(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun toggleRecognition(word: String) {
        if (recognizer.isRecognizing()) {
            recognizer.stopRecognition()
            silenceTimerJob?.cancel()
            _state.update { it.copy(isRecording = false) }
        } else {
            recognizer.startRecognition(
                onText = { text ->
                    println("Recognized: $text")
                    _state.update { it.copy(isRecording = true) }
                    val progress = state.value.progress
                    val item = state.value.sentenceItems[progress]
                    submitAnswer(
                        word = word,
                        progress = progress,
                        mode = state.value.mode,
                        origin = item.sentence,
                        userInput = text
                    )
                    restartSilenceTimer()
                },
                onFinished = {
                    _state.update { it.copy(isRecording = false) }
                    println("Recognition finished due to silence.")
                }
            )
            restartSilenceTimer()
        }
    }

    private fun restartSilenceTimer() {
        silenceTimerJob?.cancel()
        silenceTimerJob = viewModelScope.launch {
            delay(3000) // 3초간 입력 없으면 중단
            recognizer.stopRecognition()
        }
    }
}