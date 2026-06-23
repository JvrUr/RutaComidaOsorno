package osornogourmet.data.repository

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import osornogourmet.data.database.RoutesTable
import osornogourmet.data.mapper.RouteMapper
import osornogourmet.domain.model.Route
import osornogourmet.domain.repository.IRouteRepository

class RouteRepository : IRouteRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun findAll(): List<Route> = dbQuery {
        RoutesTable.selectAll().map { RouteMapper.toDomain(it) }
    }

    override suspend fun findById(id: Long): Route? = dbQuery {
        RoutesTable
            .select { RoutesTable.id eq id }
            .map { RouteMapper.toDomain(it) }
            .singleOrNull()
    }

    override suspend fun insert(route: Route): Long = dbQuery {
        RoutesTable.insertAndGetId {
            it[nombre] = route.name
            it[descripcion] = route.description
            it[idsLugaresComida] = RouteMapper.idsToJson(route.foodPlaceIds)
            it[creadoPorUsuarioId] = route.createdByUserId
            it[duracionEstimada] = route.estimatedDuration
            it[distanciaEstimada] = route.estimatedDistance
        }.value
    }

    override suspend fun update(id: Long, route: Route): Boolean = dbQuery {
        RoutesTable.update({ RoutesTable.id eq id }) {
            it[nombre] = route.name
            it[descripcion] = route.description
            it[idsLugaresComida] = RouteMapper.idsToJson(route.foodPlaceIds)
            it[duracionEstimada] = route.estimatedDuration
            it[distanciaEstimada] = route.estimatedDistance
        } > 0
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        RoutesTable.deleteWhere { RoutesTable.id eq id } > 0
    }
}
