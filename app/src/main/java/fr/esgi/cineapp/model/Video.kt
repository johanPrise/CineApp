package fr.esgi.cineapp.model

/**
 * Modèle représentant une vidéo (bande-annonce YouTube, teaser, etc.)
 */
data class Video(
    val id: String,
    val key: String, // Clé de la vidéo YouTube (ex: dQw4w9WgXcQ)
    val name: String,
    val site: String, // "YouTube"
    val type: String, // "Trailer", "Teaser", etc.
    val official: Boolean = false
) {
    val youtubeUrl: String
        get() = "https://www.youtube.com/watch?v=$key"
}
