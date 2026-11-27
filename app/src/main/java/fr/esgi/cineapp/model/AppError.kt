package fr.esgi.cineapp.model


sealed class AppError {
    data object NoConnection : AppError()
    data object Timeout : AppError()
    data class RateLimited(val retryAfterSec: Long?) : AppError()
    data object Unauthorized : AppError()
    data object NotFound : AppError()
    data class Server(val code: Int) : AppError()
    data class Unknown(val cause: Throwable) : AppError()
}