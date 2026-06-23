# OsornoGourmet - Ruta Gastronómica

OsornoGourmet es una aplicación móvil nativa en Android diseñada para descubrir y planificar rutas gastronómicas en la ciudad de Osorno, Chile. La aplicación proporciona un CRUD completo para gestionar locales adheridos, permite a los usuarios crear rutas personalizadas y traza dichos recorridos en un mapa interactivo utilizando herramientas gratuitas.

## Avances Implementados

*   **Autenticación Local:**
    *   Pantallas de Login y Registro funcionales.
    *   Persistencia de credenciales usando Room.
*   **Gestión de Locales (CRUD):**
    *   Visualización de locales con tarjetas informativas.
    *   Filtro interactivo por categorías (Restaurantes, Cafeterías, etc.).
    *   Creación, edición y eliminación de locales.
    *   *Seeder* inicial: La base de datos se precarga automáticamente con 15 locales reales populares de Osorno (Bavaria, Café del Centro, Club Alemán, Wufehr, Bitte Brot, Pistacho, entre otros).
*   **Rutas Gastronómicas:**
    *   Creación de recorridos gastronómicos personalizados.
    *   Selección de múltiples locales desde un listado interactivo.
    *   Asignación de nombre y descripción a cada ruta.
*   **Mapa Interactivo (100% Gratuito):**
    *   Implementación de mapa base usando `osmdroid` (OpenStreetMap).
    *   Visualización de todos los locales de comida como marcadores interactivos.
    *   Trazado de líneas (Polylines) para dibujar la ruta entre los locales seleccionados, haciendo uso de la API gratuita de **OpenRouteService**.
*   **UI/UX:**
    *   Diseño moderno utilizando Jetpack Compose y Material Design 3.
    *   Tema oscuro (Dark Mode) adaptado con una paleta de colores cálidos y "gastronómicos" (naranjas, rojos, marrones, blancos crema).
    *   Uso de emojis e iconos descriptivos para categorías de comida.

## Arquitectura

El proyecto sigue los principios de **Clean Architecture** (Arquitectura Limpia) y el patrón de diseño **MVVM**, separando responsabilidades en tres capas principales:

1.  **Capa Presentation (UI + MVVM):** Contiene la UI declarativa hecha en Jetpack Compose, Navegación y los ViewModels que se comunican a través de `StateFlow`.
2.  **Capa Domain (Negocio):** Capa pura en Kotlin. Define Modelos (User, FoodPlace, Route), interfaces de Repositorios y Casos de Uso (`UseCases`). No tiene dependencias de Android.
3.  **Capa Data (Persistencia y Red):** Maneja la persistencia local con **Room DB**, llamadas a la API mediante **Retrofit** y la implementación concreta de los repositorios de la capa Domain. Utiliza Patrón Singleton para la base de datos y Mappers para convertir Entities en Modelos de dominio.

## Patrones de Diseño Utilizados

*   **Repository:** Abstracción del acceso a los datos. La capa de presentación y dominio no sabe si los datos vienen de Room o de una API.
*   **Observer:** Implementado mediante `StateFlow` en los ViewModels. La UI reacciona automáticamente a los cambios de estado.
*   **Factory:** Uso de `ViewModelProvider.Factory` para la inyección de dependencias manual (sin Hilt/Koin para mantener el proyecto ligero).
*   **Singleton:** Instancia única asegurada para la base de datos `AppDatabase` y el cliente HTTP de Retrofit.

## Principios SOLID Aplicados

*   **S - Single Responsibility Principle (SRP):** Cada clase tiene una única responsabilidad. Por ejemplo, `GetFoodPlacesUseCase` sólo obtiene locales, `UpdateFoodPlaceUseCase` sólo actualiza.
*   **O - Open/Closed Principle (OCP):** Pantallas como `FoodPlaceFormScreen` y `RouteFormScreen` están diseñadas para ser reutilizadas tanto en la *creación* como en la *edición* sin necesidad de modificar su código base interno, simplemente pasándoles el ID correcto.
*   **L - Liskov Substitution Principle (LSP):** Las interfaces de los repositorios (ej. `FoodPlaceRepository`) pueden ser reemplazadas por cualquier implementación concreta (ej. Mock o Room) sin que la aplicación falle.
*   **I - Interface Segregation Principle (ISP):** Interfaces pequeñas y específicas (`UserRepository`, `RouteRepository`) en lugar de una interfaz gigantesca.
*   **D - Dependency Inversion Principle (DIP):** Los Casos de Uso (capa de Dominio, nivel alto) dependen de interfaces de Repositorio (abstracciones), no de las implementaciones concretas como Room o Retrofit (nivel bajo).

## Estructura de Paquetes

```text
com.example.osornogourmet
├── OsornoGourmetApp.kt               // Application class (Inicialización)
├── data/
│   ├── local/
│   │   ├── dao/                      // Interfaces DAO de Room
│   │   ├── entity/                   // Entidades Room (Tablas)
│   │   ├── AppDatabase.kt            // Base de datos Singleton
│   │   ├── Converters.kt             // TypeConverters (JSON)
│   │   ├── FoodPlaceSeeder.kt        // Datos precargados
│   │   └── Mappers.kt                // Conversión Entity <-> Model
│   ├── remote/
│   │   ├── OpenRouteServiceApi.kt    // Endpoints de la API
│   │   ├── OrsModels.kt              // Data classes JSON
│   │   ├── OrsRetrofitClient.kt      // Cliente HTTP
│   │   └── PolylineDecoder.kt        // Decodificador de rutas
│   └── repository/                   // Implementaciones de Repositorios
├── domain/
│   ├── model/                        // Data classes y Enums
│   ├── repository/                   // Interfaces puras
│   └── usecase/                      // Casos de uso (Lógica de negocio)
│       ├── auth/
│       ├── foodplace/
│       └── route/
└── presentation/
    ├── MainActivity.kt               // Punto de entrada Android
    ├── navigation/
    │   └── NavGraph.kt               // Definición de Rutas (Compose Navigation)
    ├── theme/                        // Color, Theme, Type
    ├── ui/                           // Pantallas (Screens) Compose
    │   ├── auth/
    │   ├── foodplace/
    │   ├── home/
    │   ├── map/
    │   └── route/
    └── viewmodel/                    // Lógica de Presentación y Estado
```
