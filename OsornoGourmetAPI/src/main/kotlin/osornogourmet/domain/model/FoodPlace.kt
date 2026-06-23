package osornogourmet.domain.model

data class FoodPlace(
    val id: Long = 0,
    val name: String,
    val description: String,
    val category: String, // ej. RESTAURANTE, CAFETERIA
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 0f,
    val imageUrl: String = "",
    val createdByUserId: Long = 0
)
