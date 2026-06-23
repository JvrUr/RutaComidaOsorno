package com.example.osornogourmet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.osornogourmet.data.local.dao.FoodPlaceDao
import com.example.osornogourmet.data.local.dao.RouteDao
import com.example.osornogourmet.data.local.dao.UserDao
import com.example.osornogourmet.data.local.entity.FoodPlaceEntity
import com.example.osornogourmet.data.local.entity.RouteEntity
import com.example.osornogourmet.data.local.entity.UserEntity

/**
 * Base de datos principal de la aplicación OsornoGourmet.
 *
 * Implementa el patrón Singleton para garantizar una única instancia
 * de la base de datos en toda la aplicación. Contiene las tablas de
 * usuarios, establecimientos de comida y rutas gastronómicas.
 *
 * @see UserDao
 * @see FoodPlaceDao
 * @see RouteDao
 */
@Database(
    entities = [
        UserEntity::class,
        FoodPlaceEntity::class,
        RouteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /** DAO para operaciones con usuarios */
    abstract fun userDao(): UserDao

    /** DAO para operaciones con establecimientos de comida */
    abstract fun foodPlaceDao(): FoodPlaceDao

    /** DAO para operaciones con rutas gastronómicas */
    abstract fun routeDao(): RouteDao

    companion object {
        /** Nombre del archivo de la base de datos SQLite */
        private const val DATABASE_NAME = "osorno_gourmet_db"

        /**
         * Instancia Singleton de la base de datos.
         * Se usa @Volatile para garantizar visibilidad entre hilos.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos.
         *
         * Usa doble verificación de bloqueo (double-checked locking) para
         * garantizar la creación thread-safe de la instancia Singleton.
         *
         * @param context Contexto de la aplicación.
         * @return Instancia única de [AppDatabase].
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
