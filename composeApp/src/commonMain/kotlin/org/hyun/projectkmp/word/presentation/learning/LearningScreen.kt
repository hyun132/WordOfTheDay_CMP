package org.hyun.projectkmp.word.presentation.learning

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LearningScreenRoot(
    word:String,
    onBackClick: () -> Unit
){
    LearningScreen(word)
}

@Composable
fun LearningScreen(word:String){
    Text(
        text = word
    )
}