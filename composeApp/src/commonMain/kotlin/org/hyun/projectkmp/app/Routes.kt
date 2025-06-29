package org.hyun.projectkmp.app

import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.bookmark
import wordoftheday.composeapp.generated.resources.calendar
import wordoftheday.composeapp.generated.resources.home_2
import wordoftheday.composeapp.generated.resources.profile

sealed interface Routes {
    @Serializable
    data object MainGraph : Routes

    @Serializable
    data object AuthGraph : Routes

    @Serializable
    data object Login : Routes

    @Serializable
    data object SignUp : Routes

    @Serializable
    data object ForgotPassword : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data class Word(val word: String) : Routes

    @Serializable
    data object History : Routes

    @Serializable
    data object Bookmark : Routes

    @Serializable
    data object Profile : Routes

    @Serializable
    data object CreateProfile : Routes
}


data class NavItem(
    val route: Routes,
    val label: String,
    val icon: DrawableResource
)

val bottomNavItems = listOf(
    NavItem(Routes.Home, "Home", Res.drawable.home_2),
    NavItem(Routes.History, "History", Res.drawable.calendar),
    NavItem(Routes.Bookmark, "Bookmark", Res.drawable.bookmark),
    NavItem(Routes.Profile, "Profile", Res.drawable.profile)
)