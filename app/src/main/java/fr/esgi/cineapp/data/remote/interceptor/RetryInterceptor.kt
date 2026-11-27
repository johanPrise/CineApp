package fr.esgi.cineapp.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.random.Random

private const val METHOD_GET = "GET"
private const val HEADER_RETRY_AFTER = "Retry-After"
private const val HTTP_TOO_MANY_REQUESTS = 429
private const val DEFAULT_RETRY_AFTER_SEC = 1L
private const val MAX_RETRY_AFTER_MS = 5_000L
private const val BASE_BACKOFF_MS = 500L
private const val JITTER_MS = 250L

class RetryInterceptor(
    private val maxRetries: Int = 2,
    private val sleep: (Long) -> Unit = { Thread.sleep(it) }
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.method != METHOD_GET) return chain.proceed(request)

        var response = chain.proceed(request)
        var attempt = 0
        while (attempt < maxRetries) {
            val delayMillis = retryDelayMillis(response, attempt) ?: break
            response.close()
            attempt++
            try {
                sleep(delayMillis)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw IOException("Interrupted during retry backoff", e)
            }
            response = chain.proceed(request)
        }
        return response
    }

    private fun retryDelayMillis(response: Response, attempt: Int): Long? = when (response.code) {
        HTTP_TOO_MANY_REQUESTS -> {
            val retryAfterSec = response.header(HEADER_RETRY_AFTER)?.toLongOrNull() ?: DEFAULT_RETRY_AFTER_SEC
            (retryAfterSec * 1_000L).coerceIn(0L, MAX_RETRY_AFTER_MS)
        }
        in 500..599 -> BASE_BACKOFF_MS * (1L shl attempt) + Random.nextLong(JITTER_MS)
        else -> null
    }
}