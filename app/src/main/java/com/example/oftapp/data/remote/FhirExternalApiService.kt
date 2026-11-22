package com.example.oftapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Modelos para parsear el Bundle JSON del servidor público de pruebas HAPI FHIR R4
 * (https://hapi.fhir.org/baseR4/DiagnosticReport) utilizando exclusivamente datos sintéticos.
 */
data class FhirBundleResponse(
    val resourceType: String? = null,
    val total: Int? = null,
    val entry: List<FhirBundleEntry>? = null
)

data class FhirBundleEntry(
    val fullUrl: String? = null,
    val resource: FhirDiagnosticResource? = null
)

data class FhirDiagnosticResource(
    val resourceType: String? = null,
    val id: String? = null,
    val status: String? = null,
    val effectiveDateTime: String? = null,
    val issued: String? = null,
    val code: FhirCodeableConcept? = null,
    val conclusion: String? = null
)

data class FhirCodeableConcept(
    val text: String? = null,
    val coding: List<FhirCoding>? = null
)

data class FhirCoding(
    val system: String? = null,
    val code: String? = null,
    val display: String? = null
)

/**
 * Modelo de dominio limpio para mostrar referencias clínicas externas (HAPI FHIR R4) en la UI.
 */
data class FhirReferenciaSintetica(
    val fhirId: String,
    val codigoEstandar: String,
    val nombreEstudio: String,
    val estadoFhir: String,
    val fechaReferencia: String
)

/**
 * Interfaz Retrofit para consumir la API REST externa pública HAPI FHIR R4.
 * Cumple con IE 3.1.4: "Consumir al menos una API REST externa pertinente, pública o de pruebas
 * (p. ej. HAPI FHIR de prueba), utilizando solo datos sintéticos".
 */
interface FhirExternalApiService {

    @GET("DiagnosticReport")
    suspend fun consultarReportesDiagnosticosFhir(
        @Query("_count") count: Int = 4,
        @Query("_format") format: String = "json"
    ): FhirBundleResponse
}
