package fr.esgi.cineapp.ui.util

object TmdbImage {
    private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"

    object Size {
        const val POSTER_SMALL = "w185"
        const val POSTER_MEDIUM = "w342"
        const val POSTER_LARGE = "w500"
        const val BACKDROP_MEDIUM = "w780"
        const val BACKDROP_LARGE = "w1280"
        const val ORIGINAL = "original"
    }

    fun posterUrl(path: String?, size: String = Size.POSTER_MEDIUM): String? {
        if (path.isNullOrBlank()) return null
        val cleanPath = if (path.startsWith("/")) path else "/$path"
        return "$BASE_IMAGE_URL$size$cleanPath"
    }

    fun backdropUrl(path: String?, size: String = Size.BACKDROP_MEDIUM): String? {
        if (path.isNullOrBlank()) return null
        val cleanPath = if (path.startsWith("/")) path else "/$path"
        return "$BASE_IMAGE_URL$size$cleanPath"
    }
}
