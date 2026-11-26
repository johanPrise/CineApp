package fr.esgi.cineapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.cineapp.data.repository.AuthRepository
import fr.esgi.cineapp.data.repository.FirebaseAuthRepositoryImpl
import fr.esgi.cineapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val MIN_PASSWORD_LENGTH = 6
private const val MSG_EMPTY_FIELDS = "Veuillez remplir tous les champs"

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<User?> = authRepository.getCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return fail(MSG_EMPTY_FIELDS)

        authenticate(fallbackMessage = "Erreur de connexion") {
            authRepository.login(email.trim(), password)
        }
    }

    fun register(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) return fail(MSG_EMPTY_FIELDS)
        if (password.length < MIN_PASSWORD_LENGTH) {
            return fail("Le mot de passe doit contenir au moins $MIN_PASSWORD_LENGTH caractères")
        }

        authenticate(fallbackMessage = "Erreur lors de l'inscription") {
            authRepository.register(username.trim(), email.trim(), password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState.Idle
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun fail(message: String) {
        _uiState.value = AuthUiState.Error(message)
    }

    private fun authenticate(fallbackMessage: String, action: suspend () -> Result<User>) {
        if (_uiState.value is AuthUiState.Loading) return // évite deux requêtes en parallèle (double tap)

        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            _uiState.value = action().fold(
                onSuccess = { AuthUiState.Success(it) },
                onFailure = { AuthUiState.Error(it.localizedMessage ?: fallbackMessage) }
            )
        }
    }
}