package fr.esgi.cineapp.data.remote

import android.content.Context
import fr.esgi.cineapp.BuildConfig
import fr.esgi.cineapp.data.remote.interceptor.LanguageInterceptor
import fr.esgi.cineapp.data.remote.interceptor.RetryInterceptor
import fr.esgi.cineapp.data.remote.interceptor.TmdbAuthInterceptor
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object TmdbNetworkClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            explicitNulls = false
        }
    }

    fun createOkHttpClient(cacheDir: File,
                           token: String = BuildConfig.TMDB_ACCESS_TOKEN
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .cache(Cache(cacheDir, 10L * 1024 * 1024))
            .addInterceptor(TmdbAuthInterceptor(BuildConfig.TMDB_ACCESS_TOKEN))
            .addInterceptor(LanguageInterceptor())
            .addInterceptor(TmdbAuthInterceptor(token))

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
                redactHeader("Authorization")
            }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    fun createApiService(okHttpClient: OkHttpClient, baseUrl: String = BASE_URL): TmdbApiService {
        val mediaType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .build()

        return retrofit.create(TmdbApiService::class.java)
    }
}
