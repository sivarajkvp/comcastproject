package com.comcostproject.data.datasource

import com.comcostproject.data.api.ApiService
import com.comcostproject.data.model.AnimalApiResponse
import retrofit2.Response
import javax.inject.Inject

class AnimalDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    AnimalRemoteDataResource {
    override suspend fun getAnimalByName(animalName: String): Response<List<AnimalApiResponse>> {
        return apiService.getAnimalsData(animalName)
    }
}