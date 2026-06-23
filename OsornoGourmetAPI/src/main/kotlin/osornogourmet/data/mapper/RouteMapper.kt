package osornogourmet.data.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import osornogourmet.data.database.RoutesTable
import osornogourmet.data.dto.RouteResponse
import osornogourmet.domain.model.Route

object RouteMapper {
    fun toDomain(row: ResultRow): Route {
        val idsJson = row[RoutesTable.idsLugaresComida]
        val idsList = try {
            Json.decodeFromString<List<Long>>(idsJson)
        } catch (e: Exception) {
            emptyList()
        }

        return Route(
            id = row[RoutesTable.id].value,
            name = row[RoutesTable.nombre],
            description = row[RoutesTable.descripcion],
            foodPlaceIds = idsList,
            createdByUserId = row[RoutesTable.creadoPorUsuarioId],
            estimatedDuration = row[RoutesTable.duracionEstimada],
            estimatedDistance = row[RoutesTable.distanciaEstimada]
        )
    }

    fun toResponse(domain: Route): RouteResponse {
        return RouteResponse(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            foodPlaceIds = domain.foodPlaceIds,
            createdByUserId = domain.createdByUserId,
            estimatedDuration = domain.estimatedDuration,
            estimatedDistance = domain.estimatedDistance
        )
    }

    fun idsToJson(ids: List<Long>): String {
        return Json.encodeToString(ids)
    }
}
