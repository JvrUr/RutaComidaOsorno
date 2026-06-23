package osornogourmet.domain.model

data class Route(
    val id: Long = 0,
    val name: String,
    val description: String,
    val foodPlaceIds: List<Long> = emptyList(),
    val createdByUserId: Long = 0,
    val estimatedDuration: String = "",
    val estimatedDistance: String = ""
)
