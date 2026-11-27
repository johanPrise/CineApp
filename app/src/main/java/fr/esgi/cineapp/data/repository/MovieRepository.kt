package fr.esgi.cineapp.data.repository

import androidx.paging.PagingData
import fr.esgi.cineapp.model.Genre
import fr.esgi.cineapp.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<PagingData<Movie>>
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getTopRatedMovies(): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(): Flow<PagingData<Movie>>
    fun searchMovies(query: String): Flow<PagingData<Movie>>

    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    suspend fun getMovieGenres(): Result<List<Genre>>
}
