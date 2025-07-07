package org.hyun.projectkmp.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.eye
import wordoftheday.composeapp.generated.resources.eye_slash

@Composable
fun PasswordField(
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