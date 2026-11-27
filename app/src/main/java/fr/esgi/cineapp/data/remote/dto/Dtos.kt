package fr.esgi.cineapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedResponseDto<T>(
    val page: Int = 1,
    val results: List<T> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 1,
    @SerialName("total_results") val totalResults: Int = 0
)

@Serializable
data class MovieDto(
    val id: Int,
    val title: String? = null,
    val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    val runtime: Int? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    val genres: List<GenreDto> = emptyList(),
    val videos: VideoResponseDto? = null
)

@Serializable
data class GenreListResponseDto(
    val genres: List<GenreDto> = emptyList()
)
@Serializable
data class GenreDto(
    val id: Int,
    val name: String =""
)

@Serializable
data class VideoResponseDto(
    val results: List<VideoDto> = emptyList()
)

@Serializable
data class VideoDto(
    val id: String="",
    val key: String="",
    val name: String = "",
    val site: String = "",
    val type: String = "",
    val official: Boolean = false,
    @SerialName("iso_639_1") val language: String? = null
)
