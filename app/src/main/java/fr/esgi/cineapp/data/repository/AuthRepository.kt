package fr.esgi.cineapp.data.repository

import fr.esgi.cineapp.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login (email: String, password: String): Result<User>
    suspend fun register(username: String, email: String, password: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
}