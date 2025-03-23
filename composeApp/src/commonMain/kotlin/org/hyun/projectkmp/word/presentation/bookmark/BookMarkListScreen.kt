package org.hyun.projectkmp.word.presentation.bookmark

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookMarkListScreenRoot(
    onBackClick: () -> Unit
){
    BookMarkListScreen()
}

@Composable
fun BookMarkListScreen(){
    Text("BookMark")
}