package com.comcostproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comcostproject.data.model.AnimalApiResponse
import com.comcostproject.data.model.AnimalDisplayModel
import com.comcostproject.data.model.ResourceState
import com.comcostproject.data.repository.AnimalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalViewModel @Inject constructor(private val animalRepository: AnimalRepository) :
    ViewModel() {

    private val _animalData: MutableStateFlow<ResourceState<List<AnimalDisplayModel>>> =
        MutableStateFlow(ResourceState.Loading())
    val animalData: StateFlow<ResourceState<List<AnimalDisplayModel>>> = _animalData

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var allAnimals: List<AnimalDisplayModel> = emptyList()

    init {
        fetchMultipleAnimals()
    }

    private fun fetchMultipleAnimals() {
        viewModelScope.launch(Dispatchers.IO) {
            animalRepository.getMultipleAnimalData(listOf("dog", "bird", "bug")).collect { response ->
                when (response) {
                    is ResourceState.Success -> {
                        val displayData = response.data.map { mapAnimalToDisplay(it) }
                        allAnimals = displayData
                        filterAnimals()
                    }
                    is ResourceState.Error -> _animalData.value = ResourceState.Error(response.error)
                    is ResourceState.Loading -> _animalData.value = ResourceState.Loading()
                }
            }
        }
    }

    private fun mapAnimalToDisplay(animal: AnimalApiResponse): AnimalDisplayModel {
        val commonFields = AnimalDisplayModel(
            name = animal.name ?: "Unknown",
            phylum = "Phylum: ${animal.taxonomy?.phylum ?: "N/A"}",
            scientificName = "Scientific Name: ${animal.taxonomy?.scientificName ?: "N/A"}",
            extraDetail1 = "",
            extraDetail2 = ""
        )

        return when {
            animal.name?.contains("dog", true) == true -> commonFields.copy(
                extraDetail1 = "Slogan: ${animal.characteristics?.slogan ?: "N/A"}",
                extraDetail2 = "Lifespan: ${animal.characteristics?.lifespan ?: "N/A"}"
            )
            animal.name?.contains("bird", true) == true -> commonFields.copy(
                extraDetail1 = "Wingspan: ${animal.characteristics?.topSpeed ?: "N/A"}", // Assuming topSpeed is a proxy for wingspan
                extraDetail2 = "Habitat: ${animal.characteristics?.habitat ?: "N/A"}"
            )
            animal.name?.contains("bug", true) == true -> commonFields.copy(
                extraDetail1 = "Prey: ${animal.characteristics?.prey ?: "N/A"}",
                extraDetail2 = "Predators: ${animal.characteristics?.biggestThreat ?: "N/A"}"
            )
            else -> commonFields.copy(
                extraDetail1 = "Unknown Type",
                extraDetail2 = ""
            )
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filterAnimals()
    }

    private fun filterAnimals() {
        val query = _searchQuery.value.lowercase()
        _animalData.value = ResourceState.Success(
            allAnimals.filter {
                it.name.lowercase().contains(query) ||
                        it.extraDetail1.lowercase().contains(query) ||
                        it.extraDetail2.lowercase().contains(query)
            }
        )
    }
}