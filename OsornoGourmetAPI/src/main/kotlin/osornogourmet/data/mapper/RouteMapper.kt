package osornogourmet.data.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import osornogourmet.data.database.RoutesTable
import osornogourmet.data.dto.RouteResponse
import osornogourmet.domain.model.Route

object RouteMapper {
    fun toDomain(row: ResultRow): Route {
        val idsJson = row[RoutesTable.foodPlaceIds]
        val idsList = try {
            Json.decodeFromString<List<Long>>(idsJson)
        } catch (e: Exception) {
            emptyList()
        }

        return Route(
            id = row[RoutesTable.id].value,
            name = row[RoutesTable.name],
            description = row[RoutesTable.description],
            foodPlaceIds = idsList,
            createdByUserId = row[RoutesTable.createdByUserId],
            estimatedDuration = row[RoutesTable.estimatedDuration],
            estimatedDistance = row[RoutesTable.estimatedDistance]
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
