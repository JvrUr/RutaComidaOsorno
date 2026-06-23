package com.example.osornogourmet

import android.app.Application
import com.example.osornogourmet.data.local.AppDatabase
import com.example.osornogourmet.data.local.FoodPlaceSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Application class de OsornoGourmet
 * Utilizada para inicializar componentes globales como la base de datos
 */
class OsornoGourmetApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar la base de datos y poblar con datos de prueba si está vacía
        val database = AppDatabase.getInstance(this)
        
        CoroutineScope(Dispatchers.IO).launch {
            FoodPlaceSeeder.seedIfEmpty(database)
        }
    }
}
