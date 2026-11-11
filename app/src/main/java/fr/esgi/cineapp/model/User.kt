package fr.esgi.cineapp.model

/**
 * Modèle représentant un utilisateur de CinéApp
 */
data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val watchedCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowedByCurrentUser: Boolean = false
)
