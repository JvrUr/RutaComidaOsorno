package osornogourmet.data.database

import org.jetbrains.exposed.dao.id.LongIdTable

object RoutesTable : LongIdTable("rutas") {
    val nombre = varchar("nombre", 255)
    val descripcion = text("descripcion")
    val idsLugaresComida = text("ids_lugares_comida") // Guardaremos los IDs como JSON "[1,2,3]"
    val creadoPorUsuarioId = long("creado_por_usuario_id").references(UsersTable.id)
    val duracionEstimada = varchar("duracion_estimada", 100).default("")
    val distanciaEstimada = varchar("distancia_estimada", 100).default("")
}
