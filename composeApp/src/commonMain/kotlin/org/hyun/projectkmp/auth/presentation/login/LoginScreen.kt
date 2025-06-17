package org.hyun.projectkmp.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightPurple
import org.hyun.projectkmp.core.presentation.UiEffect
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.setting_2
import wordoftheday.composeapp.generated.resources.setting_4

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel,
    onSignUpClick: () -> Unit,
    onForgotClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarMessage = remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.ShowError -> {
                    snackbarMessage.value = effect.message
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        LoginScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    is LoginAction.OnSignupClick -> {
                        onSignUpClick()
                    }

                    is LoginAction.OnForgotClick -> {
                        onForgotClick()
                    }

                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )

        snackbarMessage.value?.let {
            println("show $it")
            Text(it)
            ErrorBanner(it, Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun ErrorBanner(message: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(LightPurple)
            .padding(16.dp)
    ) {
        Text(text = message, color = Color.White)
    }
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Word of the day",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(40.dp))
        EmailField(state, onAction)
        Spacer(modifier = Modifier.height(20.dp))
        PasswordField(state, onAction)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "forgot Password",
            color = DeepPurple,
            textAlign = TextAlign.End,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    onAction(LoginAction.OnForgotClick)
                })
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                onAction(LoginAction.OnLoginClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepPurple)
        ) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Create An Account")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign Up",
                color = DeepPurple,
                modifier = Modifier.clickable {
                    onAction(LoginAction.OnSignupClick)
                })
        }
    }

}

@Composable
private fun EmailField(state: LoginState, onAction: (LoginAction) -> Unit) {
    OutlinedTextField(
        value = state.email,
        onValueChange = {
            onAction(LoginAction.OnEmailChange(it))
        },
        placeholder = {
            Text("email")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordField(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    OutlinedTextField(
        value = state.password,
        onValueChange = {
            onAction(LoginAction.OnPasswordChange(it))
        },
        placeholder = {
            Text("password")
        },
        visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (state.showPassword) painterResource(Res.drawable.setting_2)
            else painterResource(Res.drawable.setting_4)
            IconButton(onClick = { onAction(LoginAction.TogglePasswordVisibility) }) {
                Icon(painter = icon, "toggle password")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}