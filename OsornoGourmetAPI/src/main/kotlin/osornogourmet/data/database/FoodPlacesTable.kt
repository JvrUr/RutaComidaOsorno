package osornogourmet.data.database

import org.jetbrains.exposed.dao.id.LongIdTable

object FoodPlacesTable : LongIdTable("food_places") {
    val name = varchar("name", 255)
    val description = text("description")
    val category = varchar("category", 100)
    val address = varchar("address", 255)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val rating = float("rating").default(0f)
    val imageUrl = varchar("image_url", 500).default("")
    val createdByUserId = long("created_by_user_id").references(UsersTable.id)
}
