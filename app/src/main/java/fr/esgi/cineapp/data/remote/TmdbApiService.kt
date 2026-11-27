package fr.esgi.cineapp.data.remote

import fr.esgi.cineapp.data.remote.dto.GenreListResponseDto
import fr.esgi.cineapp.data.remote.dto.MovieDto
import fr.esgi.cineapp.data.remote.dto.PagedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val DEFAULT_REGION = "FR"
private const val APPEND_VIDEOS = "videos"
private const val VIDEO_LANGUAGES = "fr,en"

interface TmdbApiService {

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1
    ): PagedResponseDto<MovieDto>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = DEFAULT_REGION
    ): PagedResponseDto<MovieDto>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = DEFAULT_REGION
    ): PagedResponseDto<MovieDto>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = DEFAULT_REGION
    ): PagedResponseDto<MovieDto>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): PagedResponseDto<MovieDto>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = APPEND_VIDEOS,
        @Query("include_video_language") includeVideoLanguage: String = VIDEO_LANGUAGES
    ): MovieDto

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): GenreListResponseDto
}
