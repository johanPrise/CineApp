package fr.esgi.cineapp.model

/**
 * Modèle représentant un film (issu de TMDB ou du cache local)
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String = "",
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val runtime: Int? = null,
    val originalLanguage: String? = null,
    val genres: List<Genre> = emptyList()
) {
    val fullPosterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

    val fullBackdropUrl: String?
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }
}

/**
 * Genre d'un film (Action, Comédie, etc.)
 */
data class Genre(
    val id: Int,
    val name: String
)
