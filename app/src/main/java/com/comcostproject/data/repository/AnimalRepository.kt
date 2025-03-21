package com.comcostproject.data.repository

import android.util.Log
import com.comcostproject.data.datasource.AnimalRemoteDataResource
import com.comcostproject.data.model.AnimalApiResponse
import com.comcostproject.data.model.ResourceState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimalRepository @Inject constructor(private val animalRemoteDataResource: AnimalRemoteDataResource) {

    fun getMultipleAnimalData(names: List<String>): Flow<ResourceState<List<AnimalApiResponse>>> {
        return flow {
            emit(ResourceState.Loading()) // Emit loading state

            try {
                // Fetch all animal data in parallel
                val responses = coroutineScope {
                    names.map { name ->
                        async { animalRemoteDataResource.getAnimalByName(name) }
                    }.awaitAll()
                }

                // Process responses
                val successData = responses
                    .filter { it.isSuccessful && it.body() != null }
                    .flatMap { it.body()!! } // Merge all valid responses

                if (successData.isNotEmpty()) {
                    emit(ResourceState.Success(successData))
                } else {
                    emit(ResourceState.Error("No data found"))
                }
            } catch (e: Exception) {
                emit(ResourceState.Error(e.localizedMessage ?: "Unknown error"))
            }
        }.catch { e ->
            Log.e("error", e.message.toString())
            emit(ResourceState.Error(e.localizedMessage ?: "Error fetching data"))
        }
    }

}