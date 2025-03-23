package org.hyun.projectkmp.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object MainGraph:Routes

    @Serializable
    data object Home:Routes

    @Serializable
    data class Word(val word:String):Routes

    @Serializable
    data object History:Routes

    @Serializable
    data object BookMark:Routes

    @Serializable
    data object Profile:Routes
}


data class NavItem(
    val route: Routes,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    NavItem(Routes.Home, "Home", Icons.Default.Home),
    NavItem(Routes.History, "History", Icons.Default.Menu),
    NavItem(Routes.History, "BookMark", Icons.Default.Favorite),
    NavItem(Routes.Profile, "Profile", Icons.Default.Person)
)