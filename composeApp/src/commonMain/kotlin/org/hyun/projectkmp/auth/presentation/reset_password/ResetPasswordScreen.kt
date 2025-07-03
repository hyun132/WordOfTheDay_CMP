package org.hyun.projectkmp.auth.presentation.reset_password

import androidx.compose.foundation.clickable
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
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.presentation.DefaultButton
import org.hyun.projectkmp.core.presentation.UiEffect
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.eye
import wordoftheday.composeapp.generated.resources.eye_slash

@Composable
fun ResetPasswordScreenRoot(
    viewModel: ResetPasswordViewModel,
    showSnackBar: (String) -> Unit,
    navigateTo: (Routes) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is UiEffect.NavigateTo -> navigateTo(it.destination)
                is UiEffect.ShowError -> showSnackBar(it.message)
            }
        }
    }

    ResetPasswordScreen(state) {
        viewModel.onAction(it)
    }
}

@Composable
fun ResetPasswordScreen(state: ResetPasswordState, onAction: (ResetPasswordAction) -> Unit) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Forgot Password?",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.email,
            onValueChange = { onAction(ResetPasswordAction.OnEmailChange(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "email")
            }
        )
        if (!state.isCodeRequested) {
            Spacer(modifier = Modifier.height(20.dp))
            DefaultButton(
                text = "Send Code",
                onClick = { onAction(ResetPasswordAction.OnSendEmailClick) }
            )
        } else {
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = state.code,
                onValueChange = { onAction(ResetPasswordAction.OnCodeChange(it)) },
                enabled = !state.isEmailVerified,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "code")
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (!state.isEmailVerified) {
                DefaultButton(
                    text = "Verify Code",
                    onClick = { onAction(ResetPasswordAction.OnVerifyClick) },
                )
            } else {
                var isPasswordShown by remember { mutableStateOf(false) }
                var isConfirmPasswordShown by remember { mutableStateOf(false) }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onAction(ResetPasswordAction.OnPasswordChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isPasswordShown) VisualTransformation.None else PasswordVisualTransformation(),
                    placeholder = {
                        Text(text = "Password")
                    },
                    trailingIcon = {
                        val res = if (isPasswordShown) Res.drawable.eye else Res.drawable.eye_slash
                        Icon(
                            painter = painterResource(res),
                            contentDescription = "password visibility",
                            modifier = Modifier.size(20.dp)
                                .clickable { isPasswordShown = !isPasswordShown },
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = { onAction(ResetPasswordAction.OnConfirmPasswordChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isConfirmPasswordShown) VisualTransformation.None else PasswordVisualTransformation(),
                    placeholder = {
                        Text(text = "Confirm Password")
                    },
                    trailingIcon = {
                        val res =
                            if (isConfirmPasswordShown) Res.drawable.eye else Res.drawable.eye_slash
                        Icon(
                            painter = painterResource(res),
                            contentDescription = "password visibility",
                            modifier = Modifier.size(20.dp).clickable { isConfirmPasswordShown = !isConfirmPasswordShown },
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                DefaultButton(
                    text = "Save Password",
                    onClick = { onAction(ResetPasswordAction.OnSaveClick) }
                )
            }
        }
    }
}