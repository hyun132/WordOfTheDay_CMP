package org.hyun.projectkmp.word.presentation.profile

import org.hyun.projectkmp.word.presentation.learning.LearningAction

interface ProfileAction {
    data object OnBackClick: ProfileAction
    data object OnEditClick: ProfileAction
}