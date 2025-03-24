package org.hyun.projectkmp.word.presentation.bookmark

sealed interface BookmarkAction {
    data object OnBackClick:BookmarkAction
    data class OnBookMarkClick(val sentence: String):BookmarkAction
}