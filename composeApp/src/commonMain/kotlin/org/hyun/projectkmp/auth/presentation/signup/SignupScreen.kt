package org.hyun.projectkmp.auth.presentation.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.auth.presentation.login.DefaultButton
import org.hyun.projectkmp.core.presentation.UiEffect
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.eye
import wordoftheday.composeapp.generated.resources.eye_slash

@Composable
fun SignupScreenRoot(
    viewModel: SignupViewModel,
    showSnackBar: (String)->Unit,
    navigateTo: (Routes) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.ShowError -> {
                    showSnackBar(effect.message)
                }

                is UiEffect.NavigateTo -> {
                    if (effect.destination == Routes.Login) navigateTo(effect.destination)
                }

                else -> Unit
            }
        }
    }

    SignupScreen(state) { action ->
        when (action) {
            is SignupAction.OnBackPress -> {
                navigateTo(Routes.Login)
            }

            else -> Unit
        }
        viewModel.onAction(action)
    }
}

@Composable
fun SignupScreen(state: SignupState, onAction: (SignupAction) -> Unit) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Create an account",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(40.dp))
        EmailField(state.email) { text -> onAction(SignupAction.OnEmailChange(text)) }
        Spacer(modifier = Modifier.height(20.dp))
        PasswordField(
            state.password,
            state.showPassword,
            { text -> onAction(SignupAction.OnPasswordChange(text)) },
            { onAction(SignupAction.TogglePasswordVisibility) })
        Spacer(modifier = Modifier.height(20.dp))
        PasswordField(
            state.confirmPassword,
            state.showConfirmPassword,
            { text -> onAction(SignupAction.OnPasswordConfirmChange(text)) },
            { onAction(SignupAction.TogglePasswordConfirmVisibility) })
        Spacer(modifier = Modifier.height(4.dp))
        DefaultButton("Create Account") {
            onAction(SignupAction.OnSignupClick)
        }
    }
}

@Composable
private fun EmailField(email: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text("email")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordField(
    password: String,
    showPassword: Boolean,
    onTextChange: (String) -> Unit,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text("password")
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (showPassword) painterResource(Res.drawable.eye)
            else painterResource(Res.drawable.eye_slash)
            IconButton(onClick = { onClick() }) {
                Icon(painter = icon, "toggle password", modifier = Modifier.size(20.dp))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}