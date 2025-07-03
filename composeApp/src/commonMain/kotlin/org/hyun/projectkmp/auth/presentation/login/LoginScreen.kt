package org.hyun.projectkmp.auth.presentation.login

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.DefaultButton
import org.hyun.projectkmp.core.presentation.UiEffect
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.eye
import wordoftheday.composeapp.generated.resources.eye_slash

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel,
    showSnackBar: (String) -> Unit,
    navigateTo: (Routes) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.ShowError -> {
                    showSnackBar(effect.message)
                }

                is UiEffect.NavigateTo -> {
                    navigateTo(effect.destination)
                }

                else -> Unit
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
                        navigateTo(Routes.SignUp)
                    }

                    is LoginAction.OnForgotClick -> {
                        navigateTo(Routes.ResetPassword)
                    }

                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )

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
        DefaultButton("Login") {
            onAction(LoginAction.OnLoginClick)
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
            val icon = if (state.showPassword) painterResource(Res.drawable.eye)
            else painterResource(Res.drawable.eye_slash)
            IconButton(onClick = { onAction(LoginAction.TogglePasswordVisibility) }) {
                Icon(painter = icon, "toggle password", modifier = Modifier.size(20.dp))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}