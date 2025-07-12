package org.hyun.projectkmp.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.hyun.projectkmp.auth.presentation.login.LoginScreenRoot
import org.hyun.projectkmp.auth.presentation.login.LoginViewModel
import org.hyun.projectkmp.auth.presentation.reset_password.ResetPasswordScreenRoot
import org.hyun.projectkmp.auth.presentation.reset_password.ResetPasswordViewModel
import org.hyun.projectkmp.auth.presentation.signup.SignupScreenRoot
import org.hyun.projectkmp.auth.presentation.signup.SignupViewModel
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray
import org.hyun.projectkmp.core.presentation.ToastDurationType
import org.hyun.projectkmp.core.presentation.ToastManager
import org.hyun.projectkmp.word.presentation.WordHomeScreenRoot
import org.hyun.projectkmp.word.presentation.WordHomeViewModel
import org.hyun.projectkmp.word.presentation.bookmark.BookMarkListScreenRoot
import org.hyun.projectkmp.word.presentation.bookmark.BookmarkViewModel
import org.hyun.projectkmp.word.presentation.history.HistoryScreenRoot
import org.hyun.projectkmp.word.presentation.history.HistoryViewModel
import org.hyun.projectkmp.word.presentation.learning.LearningScreenRoot
import org.hyun.projectkmp.word.presentation.learning.LearningViewModel
import org.hyun.projectkmp.word.presentation.profile.ProfileScreenRoot
import org.hyun.projectkmp.word.presentation.profile.ProfileViewModel
import org.hyun.projectkmp.word.presentation.profile.create.CreateProfileScreenRoot
import org.hyun.projectkmp.word.presentation.profile.create.CreateProfileViewModel
import org.hyun.projectkmp.word.presentation.profile.update.UpdateProfileScreenRoot
import org.hyun.projectkmp.word.presentation.profile.update.UpdateProfileViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val toastManager by remember { mutableStateOf(ToastManager()) }
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
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValue ->
            NavHost(
                navController = navController,
                startDestination = Routes.AuthGraph,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {
                navigation<Routes.AuthGraph>(
                    startDestination = Routes.Login
                ) {
                    composable<Routes.Login> {
                        val viewModel = koinViewModel<LoginViewModel>()
                        LoginScreenRoot(
                            viewModel = viewModel,
                            showSnackBar = { message ->
//                                coroutineScope.launch {
//                                    snackbarHostState.showSnackbar(message = message)
//                                }
                                toastManager.showToast(message, ToastDurationType.SHORT)
                            }
                        ) { route ->
                            navController.navigate(route) {
                                if (route is Routes.MainGraph) {
                                    popUpTo(0) { inclusive = true }  // 백스택의 루트까지 모두 제거
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                    composable<Routes.SignUp> {
                        val viewModel = koinViewModel<SignupViewModel>()
                        SignupScreenRoot(
                            viewModel,
                            showSnackBar = { message ->
//                                coroutineScope.launch {
//                                    snackbarHostState.showSnackbar(message = message)
//                                }
                                toastManager.showToast(message, ToastDurationType.SHORT)
                            }) {
                            navController.navigate(it)
                        }
                    }
                    composable<Routes.ResetPassword> {
                        val viewModel = koinViewModel<ResetPasswordViewModel>()
                        ResetPasswordScreenRoot(
                            viewModel = viewModel,
                            showSnackBar = { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = message)
                                }
                            }
                        ) {
                            navController.navigate(it)
                        }
                    }
                }

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
                            },
                            showSnackBar = { message ->
//                                coroutineScope.launch {
//                                    snackbarHostState.showSnackbar(message = message)
//                                }
                                toastManager.showToast(message, ToastDurationType.SHORT)
                            }
                        ) { route ->
                            navController.navigate(route)
                        }
                    }

                    composable<Routes.Word> {
                        val homeViewModel = it.sharedKoinViewModel<WordHomeViewModel>(navController)
                        val viewModel = koinViewModel<LearningViewModel>()
                        LearningScreenRoot(
                            viewModel = viewModel,
                            wordState = homeViewModel.state.collectAsState().value,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<Routes.History> {
                        val viewModel = koinViewModel<HistoryViewModel>()
                        HistoryScreenRoot(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
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
                            showSnackBar = { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = message)
                                }
                            }
                        ){ route ->
                            navController.navigate(route)
                        }
                    }

                    composable<Routes.CreateProfile> {
                        val viewModel = koinViewModel<CreateProfileViewModel>()
                        CreateProfileScreenRoot(
                            viewModel = viewModel,
                            showSnackBar = { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = message)
                                }
                            }
                        ) { route ->
                            navController.navigate(route)
                        }
                    }

                    composable<Routes.UpdateProfile> {
                        val viewModel = koinViewModel<UpdateProfileViewModel>()
                        UpdateProfileScreenRoot(
                            viewModel = viewModel,
                            showSnackBar = { message ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = message)
                                }
                            }
                        ) { route ->
                            navController.navigate(route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    NavigationBar(
        modifier = Modifier,
        containerColor = Color.White,
        tonalElevation = 12.dp
    ) {
        bottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route.toString(),
                onClick = { navController.navigate(navItem.route) },
                icon = {
                    Icon(
                        painter = painterResource(navItem.icon),
                        contentDescription = navItem.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DeepPurple,
                    unselectedIconColor = LightGray,
                    indicatorColor = Color.Transparent
                )
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