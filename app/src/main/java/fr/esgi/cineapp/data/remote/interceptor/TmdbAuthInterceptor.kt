package fr.esgi.cineapp.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class TmdbAuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}
