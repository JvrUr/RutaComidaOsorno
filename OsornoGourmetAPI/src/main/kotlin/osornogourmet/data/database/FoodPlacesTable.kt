package osornogourmet.data.database

import org.jetbrains.exposed.dao.id.LongIdTable

object FoodPlacesTable : LongIdTable("lugares_comida") {
    val nombre = varchar("nombre", 255)
    val descripcion = text("descripcion")
    val categoria = varchar("categoria", 100)
    val direccion = varchar("direccion", 255)
    val latitud = double("latitud")
    val longitud = double("longitud")
    val calificacion = float("calificacion").default(0f)
    val urlImagen = varchar("url_imagen", 500).default("")
    val creadoPorUsuarioId = long("creado_por_usuario_id").references(UsersTable.id)
}
