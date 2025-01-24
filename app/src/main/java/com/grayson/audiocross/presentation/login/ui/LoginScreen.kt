package com.grayson.audiocross.presentation.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grayson.audiocross.R
import com.grayson.audiocross.presentation.login.viewmodel.LoginViewModel
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import com.grayson.audiocross.presentation.navigator.ui.BackTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateUp: () -> Unit = {},
    onPopBackStackToMain: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = stringResource(id = R.string.login_fail_message)
    LaunchedEffect(uiState.isLogin, uiState.message, snackbarHostState) {
        if (uiState.isLogin) {
            onPopBackStackToMain()
        }
        if (uiState.message.isNotBlank()) {
            snackbarHostState.showSnackbar(uiState.message)
            viewModel.resetWord()
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopBar(
                onNavigateUp = onNavigateUp
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        snackbarHost = { SnackbarHost(snackbarHostState) { data -> Snackbar(snackbarData = data) } },
        content = { innerPadding ->
            LoadingContent(
                isLoading = uiState.isLoading,
                innerPadding = innerPadding
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                        .verticalScroll(scrollState)
                ) {
                    Spacer(Modifier.height(30.dp))
                    Text(
                        text = stringResource(id = R.string.welcome_message),
                        modifier = Modifier.padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.weight(1f))
                    TextField(
                        value = uiState.username,
                        onValueChange = { username -> viewModel.updateUsername(username) },
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp, lineHeight = 14.sp),
                        placeholder = { Text(stringResource(id = R.string.input_username_message)) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(Modifier.height(15.dp))
                    TextField(
                        value = uiState.password,
                        onValueChange = { password -> viewModel.updatePassword(password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 20.dp),
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp, lineHeight = 14.sp),
                        placeholder = { Text(stringResource(id = R.string.input_password_message)) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = {
                                viewModel.login(errorMessage)
                                keyboardController?.hide()
                            }
                        ),
                    )
                    Spacer(Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.login),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.weight(1f))
                        Button(
                            onClick = { viewModel.login(errorMessage) },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
                            contentPadding = PaddingValues(15.dp),
                            modifier = Modifier.size(55.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_arrow_right_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun LoginScreenPreview() {
    AudioCrossTheme {
        LoginScreen()
    }
}