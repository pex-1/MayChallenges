package com.example.maychallenges.ui.smoothinputflow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maychallenges.ui.theme.MayChallengesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(onBack: () -> Unit = {}) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }

    val isLandscape =
        LocalConfiguration.current.screenWidthDp > LocalConfiguration.current.screenHeightDp

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (isLandscape) {

                // ── Landscape: 2 columns ──────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Box {
                            Icon(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(4.dp),
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        // Header
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Create Account",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Fill in your details below",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }


                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FormField(
                            label = "Name",
                            value = name,
                            onValueChange = { name = it },
                            placeholder = "Your full name",
                            focusRequester = nameFocus,
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text,
                            isPassword = false,
                            onNext = { emailFocus.requestFocus() }
                        )
                        FormField(
                            label = "Email",
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "your@email.com",
                            focusRequester = emailFocus,
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email,
                            isPassword = false,
                            onNext = { passwordFocus.requestFocus() }
                        )
                        FormField(
                            label = "Password",
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "••••••",
                            focusRequester = passwordFocus,
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            onNext = { confirmPasswordFocus.requestFocus() }
                        )

                        FormField(
                            label = "Confirm Password",
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            placeholder = "••••••",
                            focusRequester = confirmPasswordFocus,
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            onNext = {}
                        )
                    }
                }
            } else {
                Box {
                    Icon(
                        modifier = Modifier
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(4.dp),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Header
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Fill in your details below",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // ── Portrait: single column ───────────────────────────
                FormField(
                    label = "Name",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Your full name",
                    focusRequester = nameFocus,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    isPassword = false,
                    onNext = { emailFocus.requestFocus() }
                )
                FormField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "your@email.com",
                    focusRequester = emailFocus,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                    isPassword = false,
                    onNext = { passwordFocus.requestFocus() }
                )
                FormField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "••••••",
                    focusRequester = passwordFocus,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    onNext = { confirmPasswordFocus.requestFocus() }
                )
                FormField(
                    label = "Confirm Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "••••••",
                    focusRequester = confirmPasswordFocus,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    onNext = {}
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A6EFC),
                    contentColor = Color.White
                )
            ) {
                Text("Submit", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    focusRequester: FocusRequester,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    isPassword: Boolean,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            },
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onNext = { onNext() }, onDone = { onNext() }),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1A6EFC),
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Portrait – Empty", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPortraitEmpty() {
    MayChallengesTheme { CreateAccountScreen() }
}

@Preview(name = "Landscape", showBackground = true, widthDp = 720, heightDp = 360)
@Composable
private fun PreviewLandscape() {
    MayChallengesTheme { CreateAccountScreen() }
}
