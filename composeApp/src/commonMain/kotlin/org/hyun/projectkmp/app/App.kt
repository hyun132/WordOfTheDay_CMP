package org.hyun.projectkmp.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.hyun.projectkmp.word.presentation.WordHomeScreenRoot
import org.hyun.projectkmp.word.presentation.WordHomeViewModel
import org.hyun.projectkmp.word.presentation.learning.LearningScreenRoot
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Routes.MainGraph
        ) {
            navigation<Routes.MainGraph>(
                startDestination = Routes.Home
            ) {
                composable<Routes.Home> {
                    val viewModel = koinViewModel<WordHomeViewModel>()

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
                    LearningScreenRoot(
                        word = it.toRoute<Routes.Word>().word,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
            }
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