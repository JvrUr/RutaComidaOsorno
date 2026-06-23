# Proyecto OsornoGourmet

Este repositorio contiene el sistema completo del proyecto universitario "OsornoGourmet", compuesto por una aplicacion movil Android y una API Backend. El proyecto busca crear una guia gastronomica de la ciudad de Osorno, permitiendo a los usuarios visualizar locales de comida, filtrarlos por categoria y generar rutas optimizadas entre ellos.

## Estructura del Proyecto

El sistema se divide en dos componentes principales, cada uno contenido en su propio directorio:

1. **OsornoGourmetAPI (Backend)**
   - Desarrollado en Kotlin utilizando el framework Ktor.
   - Proveedor de servicios RESTful para manejar la logica de negocio.
   - Utiliza la libreria Exposed para el mapeo objeto-relacional (ORM).
   - Conectado a una base de datos PostgreSQL alojada en la plataforma serverless Neon.tech.
   - Implementa seguridad mediante Password Hashing (BCrypt) y JSON Web Tokens (JWT) para la autenticacion.

2. **OsornoGourmet (Frontend Android)**
   - Aplicacion nativa de Android desarrollada en Kotlin.
   - Interfaz grafica construida de manera declarativa con Jetpack Compose.
   - Arquitectura basada en MVVM (Model-View-ViewModel) y principios de Clean Architecture.
   - Consume la API del backend mediante Retrofit.
   - Integra la libreria OSMDroid para la visualizacion de mapas de OpenStreetMap.
   - Traza recorridos exactos en el mapa utilizando la API de OpenRouteService.

## Requisitos Previos

- Android Studio (Version 2023.3.1 o superior).
- IntelliJ IDEA (Recomendado para ejecutar la API Ktor).
- Emulador de Android o dispositivo fisico.

## Como utilizar el proyecto

Para ejecutar el sistema completo, debe seguir estos pasos:

### 1. Iniciar la API Backend (OsornoGourmetAPI)
- Abra el directorio `OsornoGourmetAPI` en IntelliJ IDEA o Android Studio.
- Asegurese de descargar las dependencias de Gradle.
- Ejecute el archivo `Application.kt` (ubicado en `src/main/kotlin/osornogourmet/Application.kt`).
- El servidor iniciara de manera local en el puerto `8080` (`http://127.0.0.1:8080`).
- La base de datos ya se encuentra configurada y alojada remotamente en Neon, por lo que no es necesario instalar un servidor local de PostgreSQL.

### 2. Iniciar la Aplicacion Android (OsornoGourmet)
- Abra el directorio `OsornoGourmet` en una nueva ventana de Android Studio.
- Sincronice el proyecto con los archivos de Gradle.
- Asegurese de que la API Ktor este corriendo en segundo plano.
- Presione el boton "Run" (flecha verde) para compilar y ejecutar la aplicacion en el emulador de Android.
- Nota: La aplicacion esta configurada para apuntar a `http://10.0.2.2:8080`, que es la direccion predeterminada que usa el emulador de Android para acceder a `localhost` en su maquina.

## Caracteristicas Implementadas (Avance 2)
- Flujo completo de capas: Entidades de Base de datos -> DTOs -> Repositorios -> Servicios -> Rutas.
- Persistencia de datos real y remota mediante Neon (PostgreSQL).
- Autenticacion de usuarios mediante JWT y contrasenas encriptadas.
- Integracion de Retrofit en el cliente Android para comunicarse con la API.
- Generacion y visualizacion de rutas en mapa con coordenadas precisas.
