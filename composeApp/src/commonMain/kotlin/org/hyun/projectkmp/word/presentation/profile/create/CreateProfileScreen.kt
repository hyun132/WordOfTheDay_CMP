package org.hyun.projectkmp.word.presentation.profile.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.UiEffect
import org.hyun.projectkmp.word.domain.Difficulty


@Composable
fun CreateProfileScreenRoot(
    viewModel: CreateProfileViewModel,
    showSnackBar: (String) -> Unit,
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
                    if (effect.destination == Routes.Home) navigateTo(effect.destination)
                }

                else -> Unit
            }
        }
    }

    CreateProfileScreen(state) { action ->
        when (action) {
            is CreateProfileAction.OnBackClick -> navigateTo(Routes.Login)
        }
        viewModel.onAction(action)
    }
}

@Composable
fun CreateProfileScreen(state: CreateProfileState, onAction: (CreateProfileAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Create a Profile",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text("name")
        TextField(state.name, "name") {
            onAction(CreateProfileAction.OnNameChanged(it))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("topic")
        TextField(state.topic, "topic") {
            onAction(CreateProfileAction.OnTopicChanged(it))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("difficulty")

        OutlinedTextField(
            value = state.difficulty.name,
            enabled = false,
            modifier = Modifier.clickable { onAction(CreateProfileAction.OnMenuExpendChanged) }
                .fillMaxWidth(),
            onValueChange = {},
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor,
                disabledTextColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor
            )
        )

        Box(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)
                .wrapContentSize(Alignment.TopStart)
        ) {
            DropdownMenu(
                expanded = state.isMenuExpended,
                onDismissRequest = {
                    onAction(CreateProfileAction.OnMenuExpendChanged)
                },
                shape = OutlinedTextFieldDefaults.shape,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (difficulty in Difficulty.entries) {
                    DropdownMenuItem(
                        text = { Text(text = difficulty.name) },
                        onClick = {
                            onAction(CreateProfileAction.OnDifficultyChanged(difficulty))
                            onAction(CreateProfileAction.OnMenuExpendChanged)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        DefaultButton("Create"){
            onAction(CreateProfileAction.OnCreateClick)
        }
    }
}

@Composable
fun DefaultButton(
    text: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        colors = colors
    ) {
        Text(text = text)
    }
}

@Composable
private fun TextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(hint)
        },
        modifier = modifier.fillMaxWidth()
    )
}