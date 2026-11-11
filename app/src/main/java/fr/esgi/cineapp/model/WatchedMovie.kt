package fr.esgi.cineapp.model

/**
 * Modèle représentant un film vu par un utilisateur (pour son mur de films vus)
 */
data class WatchedMovie(
    val id: String = "",
    val userId: String = "",
    val movieId: Int = 0,
    val movieTitle: String = "",
    val moviePosterPath: String? = null,
    val userRating: Float? = null,
    val userReview: String? = null,
    val watchedAt: Long = System.currentTimeMillis()
) {
    val fullPosterUrl: String?
        get() = moviePosterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
}
