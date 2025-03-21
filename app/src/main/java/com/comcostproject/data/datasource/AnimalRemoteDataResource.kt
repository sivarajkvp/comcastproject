package com.comcostproject.data.datasource

import com.comcostproject.data.model.AnimalApiResponse
import retrofit2.Response

interface AnimalRemoteDataResource {
    suspend fun getAnimalByName(animalName: String) : Response<List<AnimalApiResponse>>
}