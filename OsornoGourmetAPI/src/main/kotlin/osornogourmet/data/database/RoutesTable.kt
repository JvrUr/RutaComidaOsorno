package osornogourmet.data.database

import org.jetbrains.exposed.dao.id.LongIdTable

object RoutesTable : LongIdTable("routes") {
    val name = varchar("name", 255)
    val description = text("description")
    val foodPlaceIds = text("food_place_ids") // Guardaremos los IDs como JSON "[1,2,3]"
    val createdByUserId = long("created_by_user_id").references(UsersTable.id)
    val estimatedDuration = varchar("estimated_duration", 100).default("")
    val estimatedDistance = varchar("estimated_distance", 100).default("")
}
