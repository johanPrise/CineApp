package fr.esgi.cineapp.data.remote

import fr.esgi.cineapp.model.AppError
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.SocketTimeoutException

private const val HTTP_TOO_MANY_REQUESTS = 429 // absent de HttpURLConnection
private const val HEADER_RETRY_AFTER = "Retry-After"

fun Throwable.toAppError(): AppError = when (this) {
    is HttpException -> toHttpAppError()
    is SocketTimeoutException -> AppError.Timeout
    is IOException -> AppError.NoConnection // UnknownHost, Connect, SSL, EOF...
    else -> AppError.Unknown(this)
}

private fun HttpException.toHttpAppError(): AppError = when (code()) {
    HTTP_UNAUTHORIZED -> AppError.Unauthorized
    HTTP_NOT_FOUND -> AppError.NotFound
    HTTP_TOO_MANY_REQUESTS ->
        AppError.RateLimited(response()?.headers()?.get(HEADER_RETRY_AFTER)?.toLongOrNull())
    in 500..599 -> AppError.Server(code())
    else -> AppError.Unknown(this)
}

class AppException(val error: AppError, cause: Throwable? = null) : Exception(cause)