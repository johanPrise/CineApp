package fr.esgi.cineapp.data.mapper

import fr.esgi.cineapp.data.remote.dto.GenreDto
import fr.esgi.cineapp.data.remote.dto.MovieDto
import fr.esgi.cineapp.data.remote.dto.VideoDto
import fr.esgi.cineapp.model.Genre
import fr.esgi.cineapp.model.Movie
import fr.esgi.cineapp.model.Video

private const val SITE_YOUTUBE = "YouTube"
private const val TYPE_TRAILER = "Trailer"
fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate?.takeIf { it.isNotBlank() },
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    runtime = runtime,
    originalLanguage = originalLanguage,
    genres = genres.map { it.toDomain() },
    videos = videos?.results.orEmpty()
        .filter { it.key.isNotBlank() }
        .map { it.toDomain() }
)

fun GenreDto.toDomain(): Genre = Genre(
    id = id,
    name = name
)

fun VideoDto.toDomain(): Video = Video(
    id = id,
    key = key,
    name = name,
    site = site,
    type = type,
    official = official,
    language = language
)

fun List<Video>.bestTrailer(): Video? {
    val trailers = filter { it.site.equals(SITE_YOUTUBE, ignoreCase = true) && it.type.equals(TYPE_TRAILER, ignoreCase = true) }
    return trailers.firstOrNull { it.official } ?: trailers.firstOrNull()
}