package osornogourmet.data.mapper

import org.jetbrains.exposed.sql.ResultRow
import osornogourmet.data.database.FoodPlacesTable
import osornogourmet.data.dto.FoodPlaceResponse
import osornogourmet.domain.model.FoodPlace

object FoodPlaceMapper {
    fun toDomain(row: ResultRow): FoodPlace {
        return FoodPlace(
            id = row[FoodPlacesTable.id].value,
            name = row[FoodPlacesTable.nombre],
            description = row[FoodPlacesTable.descripcion],
            category = row[FoodPlacesTable.categoria],
            address = row[FoodPlacesTable.direccion],
            latitude = row[FoodPlacesTable.latitud],
            longitude = row[FoodPlacesTable.longitud],
            rating = row[FoodPlacesTable.calificacion],
            imageUrl = row[FoodPlacesTable.urlImagen],
            createdByUserId = row[FoodPlacesTable.creadoPorUsuarioId]
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
