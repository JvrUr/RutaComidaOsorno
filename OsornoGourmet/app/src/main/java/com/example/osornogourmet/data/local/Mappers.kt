package com.example.osornogourmet.data.local

import com.example.osornogourmet.data.local.entity.FoodPlaceEntity
import com.example.osornogourmet.data.local.entity.RouteEntity
import com.example.osornogourmet.data.local.entity.UserEntity
import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Funciones de extensión para mapear entre entidades Room y modelos de dominio.
 *
 * Estas funciones encapsulan la lógica de conversión necesaria para mantener
 * la separación entre la capa de datos y la capa de dominio, permitiendo que
 * cada capa use sus propios modelos sin acoplamiento directo.
 */

// ========================
// Mappers de User
// ========================

/**
 * Convierte una entidad [UserEntity] de Room al modelo de dominio [User].
 *
 * @return Modelo de dominio [User] con los mismos datos.
 */
fun UserEntity.toDomain(): User = User(
    id = id,
    email = email,
    name = name,
    passwordHash = passwordHash
)

/**
 * Convierte un modelo de dominio [User] a una entidad [UserEntity] de Room.
 *
 * @return Entidad [UserEntity] lista para insertar en la base de datos.
 */
fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    email = email,
    name = name,
    passwordHash = passwordHash
)

// ========================
// Mappers de FoodPlace
// ========================

/**
 * Convierte una entidad [FoodPlaceEntity] de Room al modelo de dominio [FoodPlace].
 *
 * La categoría se convierte de String al enum [FoodCategory] usando valueOf().
 *
 * @return Modelo de dominio [FoodPlace] con la categoría como enum.
 */
fun FoodPlaceEntity.toDomain(): FoodPlace = FoodPlace(
    id = id,
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

/**
 * Convierte un modelo de dominio [FoodPlace] a una entidad [FoodPlaceEntity] de Room.
 *
 * La categoría se convierte del enum [FoodCategory] a String usando .name.
 *
 * @return Entidad [FoodPlaceEntity] con la categoría como String.
 */
fun FoodPlace.toEntity(): FoodPlaceEntity = FoodPlaceEntity(
    id = id,
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

// ========================
// Mappers de Route
// ========================

/** Instancia de Gson compartida para la serialización de listas en rutas */
private val gson = Gson()

/**
 * Convierte una entidad [RouteEntity] de Room al modelo de dominio [Route].
 *
 * El campo foodPlaceIds se deserializa de JSON String a List<Long>.
 *
 * @return Modelo de dominio [Route] con foodPlaceIds como List<Long>.
 */
fun RouteEntity.toDomain(): Route {
    val type = object : TypeToken<List<Long>>() {}.type
    val ids: List<Long> = gson.fromJson(foodPlaceIds, type)
    return Route(
        id = id,
        name = name,
        description = description,
        foodPlaceIds = ids,
        createdByUserId = createdByUserId,
        estimatedDuration = estimatedDuration,
        estimatedDistance = estimatedDistance
    )
}

/**
 * Convierte un modelo de dominio [Route] a una entidad [RouteEntity] de Room.
 *
 * El campo foodPlaceIds se serializa de List<Long> a JSON String.
 *
 * @return Entidad [RouteEntity] con foodPlaceIds como JSON String.
 */
fun Route.toEntity(): RouteEntity = RouteEntity(
    id = id,
    name = name,
    description = description,
    foodPlaceIds = gson.toJson(foodPlaceIds),
    createdByUserId = createdByUserId,
    estimatedDuration = estimatedDuration,
    estimatedDistance = estimatedDistance
)
