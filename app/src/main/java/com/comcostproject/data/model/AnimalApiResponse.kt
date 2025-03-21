package com.comcostproject.data.model

import com.google.gson.annotations.SerializedName

data class AnimalApiResponse(
    @SerializedName("name") val name: String? = null,
    @SerializedName("taxonomy") val taxonomy: Taxonomy? = null,
    @SerializedName("locations") val locations: List<String> = emptyList(),
    @SerializedName("characteristics") val characteristics: Characteristics? = null,
)

data class Characteristics(
    @SerializedName("prey") val prey: String? = null,
    @SerializedName("name_of_young") val nameOfYoung: String? = null,
    @SerializedName("group_behavior") val groupBehavior: String? = null,
    @SerializedName("estimated_population_size") val estimatedPopulationSize: String? = null,
    @SerializedName("biggest_threat") val biggestThreat: String? = null,
    @SerializedName("most_distinctive_feature") val mostDistinctiveFeature: String? = null,
    @SerializedName("gestation_period") val gestationPeriod: String? = null,
    @SerializedName("habitat") val habitat: String? = null,
    @SerializedName("diet") val diet: String? = null,
    @SerializedName("average_litter_size") val averageLitterSize: String? = null,
    @SerializedName("lifestyle") val lifestyle: String? = null,
    @SerializedName("common_name") val commonName: String? = null,
    @SerializedName("number_of_species") val numberOfSpecies: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("slogan") val slogan: String? = null,
    @SerializedName("group") val group: String? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("skin_type") val skinType: String? = null,
    @SerializedName("top_speed") val topSpeed: String? = null,
    @SerializedName("lifespan") val lifespan: String? = null,
    @SerializedName("weight") val weight: String? = null,
    @SerializedName("height") val height: String? = null,
    @SerializedName("age_of_sexual_maturity") val ageOfSexualMaturity: String? = null,
    @SerializedName("age_of_weaning") val ageOfWeaning: String? = null,
)

data class Taxonomy(
    @SerializedName("kingdom") val kingdom: String? = null,
    @SerializedName("phylum") val phylum: String? = null,
    @SerializedName("class") val classes: String? = null,
    @SerializedName("order") val order: String? = null,
    @SerializedName("family") val family: String? = null,
    @SerializedName("genus") val genus: String? = null,
    @SerializedName("scientific_name") val scientificName: String? = null,
)