package com.comcostproject.data.api


import com.comcostproject.data.model.AnimalApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("v1/animals")
    suspend fun getAnimalsData(@Query("name") name: String): Response<List<AnimalApiResponse>>
}