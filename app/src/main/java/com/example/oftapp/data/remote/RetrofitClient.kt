package com.example.oftapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Proveedor singleton de clientes Retrofit para:
 * 1. Microservicio propio Spring Boot (http://10.0.2.2:8080/ en emulador o IP configurable en dispositivo físico).
 * 2. API REST Externa Pública HAPI FHIR R4 (https://hapi.fhir.org/baseR4/).
 */
object RetrofitClient {

    @Volatile
    var backendBaseUrl: String = "http://10.0.2.2:8080/"

    private const val FHIR_EXTERNAL_BASE_URL = "https://hapi.fhir.org/baseR4/"

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    private val backendOkHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(2500, TimeUnit.MILLISECONDS)
            .readTimeout(3000, TimeUnit.MILLISECONDS)
            .writeTimeout(3000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val externalOkHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val oftAppApi: OftAppApiService
        get() = Retrofit.Builder()
            .baseUrl(backendBaseUrl)
            .client(backendOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OftAppApiService::class.java)

    val fhirExternalApi: FhirExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(FHIR_EXTERNAL_BASE_URL)
            .client(externalOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FhirExternalApiService::class.java)
    }
}
