package osornogourmet.config

object JwtConfig {
    // Para producción, esto DEBE venir de variables de entorno.
    val secret = System.getenv("JWT_SECRET") ?: "mi_secreto_super_seguro_osorno_gourmet_2026"
    val issuer = System.getenv("JWT_ISSUER") ?: "http://0.0.0.0:8080/"
    val audience = System.getenv("JWT_AUDIENCE") ?: "osornogourmet_users"
    val realm = "Access to OsornoGourmet API"
    
    // 24 horas en milisegundos
    val expirationTime: Long = 86400000 
}
