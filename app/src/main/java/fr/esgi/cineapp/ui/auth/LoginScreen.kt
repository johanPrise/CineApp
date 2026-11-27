package fr.esgi.cineapp.ui.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onLoginSuccess()
        }
    }

    fun submit() = viewModel.login(email, password)

    AuthScreenLayout(
        subtitle = "Connectez-vous pour retrouver vos films",
        modifier = modifier
    ) {
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Adresse email",
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Mot de passe",
            imeAction = ImeAction.Done,
            isPassword = true,
            onDone = { submit() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthErrorMessage((uiState as? AuthUiState.Error)?.message)

        AuthSubmitButton(
            text = "Se connecter",
            isLoading = uiState is AuthUiState.Loading,
            onClick = { submit() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                viewModel.resetState()
                onNavigateToRegister()
            }
        ) {
            Text("Pas encore de compte ? S'inscrire")
        }
    }
}
