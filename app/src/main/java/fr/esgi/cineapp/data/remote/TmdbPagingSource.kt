package fr.esgi.cineapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import fr.esgi.cineapp.data.remote.dto.PagedResponseDto
import kotlin.coroutines.cancellation.CancellationException

private const val TMDB_MAX_PAGES = 500

class TmdbPagingSource<T : Any>(
    private val fetchPage: suspend (page: Int) -> PagedResponseDto<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val response = fetchPage(page)
            val totalPages = minOf(response.totalPages, TMDB_MAX_PAGES)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (page < totalPages) page + 1 else null

            LoadResult.Page(
                data = response.results,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(AppException(e.toAppError(), e))
        }
    }
}
