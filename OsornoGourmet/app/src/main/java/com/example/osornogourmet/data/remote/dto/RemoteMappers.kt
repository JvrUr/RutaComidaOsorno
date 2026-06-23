package com.example.osornogourmet.data.remote.dto

import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.model.Route

fun FoodPlaceDto.toDomain(): FoodPlace {
    return FoodPlace(
        id = id ?: 0,
        name = name,
        description = description,
        category = FoodCategory.valueOf(category),
        address = address,
        latitude = latitude,
        longitude = longitude,
        rating = rating,
        imageUrl = imageUrl,
        createdByUserId = createdByUserId
    )
}

fun FoodPlace.toDto(): FoodPlaceDto {
    return FoodPlaceDto(
        id = if (id == 0L) null else id,
        name = name,
        description = description,
        category = category.name,
        address = address,
        latitude = latitude,
        longitude = longitude,
        rating = rating,
        imageUrl = imageUrl,
        createdByUserId = createdByUserId
    )
}

fun RouteDto.toDomain(): Route {
    return Route(
        id = id ?: 0,
        name = name,
        description = description,
        foodPlaceIds = foodPlaceIds,
        createdByUserId = createdByUserId,
        estimatedDuration = estimatedDuration,
        estimatedDistance = estimatedDistance
    )
}

fun Route.toDto(): RouteDto {
    return RouteDto(
        id = if (id == 0L) null else id,
        name = name,
        description = description,
        foodPlaceIds = foodPlaceIds,
        createdByUserId = createdByUserId,
        estimatedDuration = estimatedDuration,
        estimatedDistance = estimatedDistance
    )
}
