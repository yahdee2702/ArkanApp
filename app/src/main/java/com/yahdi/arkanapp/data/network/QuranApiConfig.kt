package com.yahdi.arkanapp.data.network

import com.google.gson.Gson
import com.yahdi.arkanapp.BuildConfig
import com.yahdi.arkanapp.data.deserializer.AyahDeserializer
import com.yahdi.arkanapp.data.deserializer.QuranDeserializer
import com.yahdi.arkanapp.data.deserializer.SearchDeserializer
import com.yahdi.arkanapp.data.deserializer.SurahDeserializer
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.response.QuranResponse
import com.yahdi.arkanapp.data.response.SearchResponse
import com.yahdi.arkanapp.data.response.SurahResponse
import com.yahdi.arkanapp.utils.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object QuranApiConfig {
    fun getApiService(): QuranApi {
        val loggingInterceptor = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(defaultHttpClient())
            .pingInterval(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val gson: Gson = Utils.getDefaultGson()
            .registerTypeAdapter(AyahResponse::class.java, AyahDeserializer())
            .registerTypeAdapter(SurahResponse::class.java, SurahDeserializer())
            .registerTypeAdapter(QuranResponse::class.java, QuranDeserializer())
            .registerTypeAdapter(SearchResponse::class.java, SearchDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(QuranApi::class.java)
    }

    @Throws(IOException::class)
    private fun defaultHttpClient(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            return@Interceptor chain.proceed(request)
        }
    }

}