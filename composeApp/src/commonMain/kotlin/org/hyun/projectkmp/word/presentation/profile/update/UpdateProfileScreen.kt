package org.hyun.projectkmp.word.presentation.profile.update

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.presentation.LightGray
import org.hyun.projectkmp.core.presentation.UiEffect
import org.hyun.projectkmp.word.domain.Difficulty
import org.hyun.projectkmp.word.presentation.components.ActionBar
import org.hyun.projectkmp.word.presentation.profile.create.DefaultButton
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.arrow_left

@Composable
fun UpdateProfileScreenRoot(
    viewModel: UpdateProfileViewModel,
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

    UpdateProfileScreen(state) { action ->
        when (action) {
            is UpdateProfileAction.OnBackClick -> navigateTo(Routes.Profile)
        }
        viewModel.onAction(action)
    }
}

@Composable
fun UpdateProfileScreen(state: UpdateProfileState, onAction: (UpdateProfileAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8E8E8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ActionBar(title = "Profile Update", leftIcon = {
                Icon(
                    painter = painterResource(Res.drawable.arrow_left),
                    "navigate back",
                    modifier = Modifier.size(20.dp)
                )
            })
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, LightGray)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("name")
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = {
                            onAction(UpdateProfileAction.OnNameChanged(it))
                        },
                        placeholder = {
                            Text("name")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("topic")
                    OutlinedTextField(
                        value = state.topic,
                        onValueChange = {
                            onAction(UpdateProfileAction.OnTopicChanged(it))
                        },
                        placeholder = {
                            Text("topic")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("difficulty")

                    OutlinedTextField(
                        value = state.difficulty.name,
                        enabled = false,
                        modifier = Modifier.clickable { onAction(UpdateProfileAction.OnMenuExpendChanged) }
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
                                onAction(UpdateProfileAction.OnMenuExpendChanged)
                            },
                            shape = OutlinedTextFieldDefaults.shape,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (difficulty in Difficulty.entries) {
                                DropdownMenuItem(
                                    text = { Text(text = difficulty.name) },
                                    onClick = {
                                        onAction(UpdateProfileAction.OnDifficultyChanged(difficulty))
                                        onAction(UpdateProfileAction.OnMenuExpendChanged)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    DefaultButton("Update") {
                        onAction(UpdateProfileAction.OnUpdateClick)
                    }
                }
            }
        }
    }
}