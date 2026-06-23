package osornogourmet.data.repository

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import osornogourmet.data.database.FoodPlacesTable
import osornogourmet.data.mapper.FoodPlaceMapper
import osornogourmet.domain.model.FoodPlace
import osornogourmet.domain.repository.IFoodPlaceRepository

class FoodPlaceRepository : IFoodPlaceRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun findAll(): List<FoodPlace> = dbQuery {
        FoodPlacesTable.selectAll().map { FoodPlaceMapper.toDomain(it) }
    }

    override suspend fun findById(id: Long): FoodPlace? = dbQuery {
        FoodPlacesTable
            .select { FoodPlacesTable.id eq id }
            .map { FoodPlaceMapper.toDomain(it) }
            .singleOrNull()
    }

    override suspend fun findByIds(ids: List<Long>): List<FoodPlace> = dbQuery {
        if (ids.isEmpty()) return@dbQuery emptyList()
        FoodPlacesTable
            .select { FoodPlacesTable.id inList ids }
            .map { FoodPlaceMapper.toDomain(it) }
    }

    override suspend fun findByCategory(category: String): List<FoodPlace> = dbQuery {
        FoodPlacesTable
            .select { FoodPlacesTable.categoria eq category }
            .map { FoodPlaceMapper.toDomain(it) }
    }

    override suspend fun insert(foodPlace: FoodPlace): Long = dbQuery {
        FoodPlacesTable.insertAndGetId {
            it[nombre] = foodPlace.name
            it[descripcion] = foodPlace.description
            it[categoria] = foodPlace.category
            it[direccion] = foodPlace.address
            it[latitud] = foodPlace.latitude
            it[longitud] = foodPlace.longitude
            it[calificacion] = foodPlace.rating
            it[urlImagen] = foodPlace.imageUrl
            it[creadoPorUsuarioId] = foodPlace.createdByUserId
        }.value
    }

    override suspend fun update(id: Long, foodPlace: FoodPlace): Boolean = dbQuery {
        FoodPlacesTable.update({ FoodPlacesTable.id eq id }) {
            it[nombre] = foodPlace.name
            it[descripcion] = foodPlace.description
            it[categoria] = foodPlace.category
            it[direccion] = foodPlace.address
            it[latitud] = foodPlace.latitude
            it[longitud] = foodPlace.longitude
            it[calificacion] = foodPlace.rating
            it[urlImagen] = foodPlace.imageUrl
        } > 0
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        FoodPlacesTable.deleteWhere { FoodPlacesTable.id eq id } > 0
    }
}
