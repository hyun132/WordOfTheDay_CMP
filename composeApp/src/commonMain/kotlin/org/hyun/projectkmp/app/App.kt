package org.hyun.projectkmp.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.Settings
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray
import org.hyun.projectkmp.word.presentation.WordHomeScreenRoot
import org.hyun.projectkmp.word.presentation.WordHomeViewModel
import org.hyun.projectkmp.word.presentation.bookmark.BookMarkListScreenRoot
import org.hyun.projectkmp.word.presentation.bookmark.BookmarkViewModel
import org.hyun.projectkmp.word.presentation.learning.LearningScreenRoot
import org.hyun.projectkmp.word.presentation.learning.LearningViewModel
import org.hyun.projectkmp.word.presentation.profile.ProfileScreenRoot
import org.hyun.projectkmp.word.presentation.profile.ProfileViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route?.split(".")
                ?.last()
        val bottomNavRoutes = bottomNavItems.map { it.label }

        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = currentRoute in bottomNavRoutes,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    if (currentRoute != null) {
                        BottomNavBar(navController, currentRoute)
                    }
                }
            }
        ) { paddingValue ->
            NavHost(
                navController = navController,
                startDestination = Routes.MainGraph,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {
                navigation<Routes.MainGraph>(
                    startDestination = Routes.Home
                ) {
                    composable<Routes.Home> {
                        val viewModel = it.sharedKoinViewModel<WordHomeViewModel>(navController)

                        WordHomeScreenRoot(
                            viewModel = viewModel,
                            onWordClick = { word ->
                                navController.navigate(
                                    Routes.Word(word = word)
                                )
                            }
                        )
                    }

                    composable<Routes.Word> {
                        val viewModel = koinViewModel<LearningViewModel>()
                        LearningScreenRoot(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<Routes.History> {
                        Text(text = "History")
                    }

                    composable<Routes.Bookmark> {
                        val viewModel = koinViewModel<BookmarkViewModel>()
                        BookMarkListScreenRoot(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<Routes.Profile> {
                        val viewModel = koinViewModel<ProfileViewModel>()
                        ProfileScreenRoot(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    BottomNavigation(
        modifier = Modifier,
        backgroundColor = Color.White,
        elevation = 12.dp
    ) {
        bottomNavItems.forEach { navItem ->
            BottomNavigationItem(
                selected = currentRoute == navItem.route.toString(),
                onClick = { navController.navigate(navItem.route) },
                icon = { Icon(navItem.icon, contentDescription = navItem.label) },
                selectedContentColor = DeepPurple,
                unselectedContentColor = LightGray
            )
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}