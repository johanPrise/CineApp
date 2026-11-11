package fr.esgi.cineapp.model

/**
 * Modèle représentant un avis et une note (1 à 5 étoiles) attribués à un film
 */
data class Review(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val userAvatarUrl: String? = null,
    val movieId: Int = 0,
    val movieTitle: String = "",
    val moviePosterPath: String? = null,
    val rating: Float = 0f,
    val comment: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
