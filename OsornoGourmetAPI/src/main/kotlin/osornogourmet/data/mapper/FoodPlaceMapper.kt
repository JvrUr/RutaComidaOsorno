package osornogourmet.data.mapper

import org.jetbrains.exposed.sql.ResultRow
import osornogourmet.data.database.FoodPlacesTable
import osornogourmet.data.dto.FoodPlaceResponse
import osornogourmet.domain.model.FoodPlace

object FoodPlaceMapper {
    fun toDomain(row: ResultRow): FoodPlace {
        return FoodPlace(
            id = row[FoodPlacesTable.id].value,
            name = row[FoodPlacesTable.name],
            description = row[FoodPlacesTable.description],
            category = row[FoodPlacesTable.category],
            address = row[FoodPlacesTable.address],
            latitude = row[FoodPlacesTable.latitude],
            longitude = row[FoodPlacesTable.longitude],
            rating = row[FoodPlacesTable.rating],
            imageUrl = row[FoodPlacesTable.imageUrl],
            createdByUserId = row[FoodPlacesTable.createdByUserId]
        )
    }

    fun toResponse(domain: FoodPlace): FoodPlaceResponse {
        return FoodPlaceResponse(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            category = domain.category,
            address = domain.address,
            latitude = domain.latitude,
            longitude = domain.longitude,
            rating = domain.rating,
            imageUrl = domain.imageUrl,
            createdByUserId = domain.createdByUserId
        )
    }
}
