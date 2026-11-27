package fr.esgi.cineapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import fr.esgi.cineapp.data.mapper.toDomain
import fr.esgi.cineapp.data.remote.AppException
import fr.esgi.cineapp.data.remote.TmdbApiService
import fr.esgi.cineapp.data.remote.TmdbPagingSource
import fr.esgi.cineapp.data.remote.dto.PagedResponseDto
import fr.esgi.cineapp.data.remote.toAppError
import fr.esgi.cineapp.model.Genre
import fr.esgi.cineapp.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DEFAULT_PAGE_SIZE = 20
private const val PREFETCH_DISTANCE = 5

class MovieRepositoryImpl(
    private val apiService: TmdbApiService
) : MovieRepository {

    private val pagingConfig = PagingConfig(
        pageSize = DEFAULT_PAGE_SIZE,
        prefetchDistance = PREFETCH_DISTANCE,
        enablePlaceholders = false
    )

    private fun <T : Any> createPager(
        fetch: suspend (page: Int) -> PagedResponseDto<T>
    ): Flow<PagingData<T>> {
        return Pager(config = pagingConfig) {
            TmdbPagingSource(fetch)
        }.flow
    }

    override fun getTrendingMovies(): Flow<PagingData<Movie>> =
        createPager { page -> apiService.getTrendingMovies(page) }
            .map { pagingData -> pagingData.map { it.toDomain() } }

    override fun getPopularMovies(): Flow<PagingData<Movie>> =
        createPager { page -> apiService.getPopularMovies(page) }
            .map { pagingData -> pagingData.map { it.toDomain() } }

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> =
        createPager { page -> apiService.getTopRatedMovies(page) }
            .map { pagingData -> pagingData.map { it.toDomain() } }

    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> =
        createPager { page -> apiService.getNowPlayingMovies(page) }
            .map { pagingData -> pagingData.map { it.toDomain() } }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> =
        createPager { page -> apiService.searchMovies(query = query, page = page) }
            .map { pagingData -> pagingData.map { it.toDomain() } }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            Result.success(apiService.getMovieDetails(movieId).toDomain())
        } catch (e: Throwable) {
            Result.failure(AppException(e.toAppError(), e))
        }
    }

    override suspend fun getMovieGenres(): Result<List<Genre>> {
        return try {
            Result.success(apiService.getMovieGenres().genres.map { it.toDomain() })
        } catch (e: Throwable) {
            Result.failure(AppException(e.toAppError(), e))
        }
    }
}
