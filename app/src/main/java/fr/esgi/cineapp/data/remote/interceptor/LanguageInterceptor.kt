package fr.esgi.cineapp.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor(private val lang: String = "fr-FR") : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        if (req.url.queryParameter("language") != null) return chain.proceed(req)
        val newUrl = req.url.newBuilder()
            .addQueryParameter("language", lang)
            .build()
        return chain.proceed(req.newBuilder().url(newUrl).build())
    }
}
