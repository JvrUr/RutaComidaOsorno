package com.example.osornogourmet.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.presentation.theme.*
import com.example.osornogourmet.presentation.viewmodel.FoodPlaceViewModel
import com.example.osornogourmet.presentation.viewmodel.RouteViewModel

/**
 * Pantalla principal (Dashboard) con resumen de locales y rutas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    foodPlaceViewModel: FoodPlaceViewModel,
    routeViewModel: RouteViewModel,
    userName: String,
    onNavigateToFoodPlaces: () -> Unit,
    onNavigateToRoutes: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToFoodPlaceDetail: (Long) -> Unit,
    onLogout: () -> Unit
) {
    val foodPlaces by foodPlaceViewModel.foodPlaces.collectAsState()
    val routes by routeViewModel.routes.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "¡Hola, $userName! 👋",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextWhite
                        )
                        Text(
                            "Descubre la gastronomía de Osorno",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = TextGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Tarjetas de resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    icon = Icons.Default.Restaurant,
                    title = "Locales",
                    count = foodPlaces.size.toString(),
                    gradient = listOf(OrangeMain, OrangeDark),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToFoodPlaces
                )
                SummaryCard(
                    icon = Icons.Default.Route,
                    title = "Rutas",
                    count = routes.size.toString(),
                    gradient = listOf(GoldAccent, OrangeMain),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToRoutes
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Accesos rápidos
            Text(
                "Acceso Rápido",
                style = MaterialTheme.typography.headlineMedium,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAccessCard(
                    icon = Icons.Default.Map,
                    title = "Ver Mapa",
                    subtitle = "Explora locales",
                    onClick = onNavigateToMap,
                    modifier = Modifier.weight(1f)
                )
                QuickAccessCard(
                    icon = Icons.Default.AddLocation,
                    title = "Nueva Ruta",
                    subtitle = "Crea un recorrido",
                    onClick = onNavigateToRoutes,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Locales destacados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Locales Destacados",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextWhite
                )
                TextButton(onClick = onNavigateToFoodPlaces) {
                    Text("Ver todos", color = OrangeMain)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (foodPlaces.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Cargando locales...",
                        modifier = Modifier.padding(24.dp),
                        color = TextGray
                    )
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(foodPlaces.take(8)) { place ->
                        FeaturedFoodPlaceCard(
                            foodPlace = place,
                            onClick = { onNavigateToFoodPlaceDetail(place.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Categorías disponibles
            Text(
                "Categorías",
                style = MaterialTheme.typography.headlineMedium,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(12.dp))

            val categoryCounts = foodPlaces.groupBy { it.category }.mapValues { it.value.size }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoryCounts.entries.toList()) { (category, count) ->
                    AssistChip(
                        onClick = onNavigateToFoodPlaces,
                        label = { Text("${category.displayName} ($count)") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = DarkCard,
                            labelColor = TextWhite
                        ),
                        border = AssistChipDefaults.assistChipBorder(
                            enabled = true,
                            borderColor = OrangeMain.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Tarjeta de resumen con gradiente
 */
@Composable
fun SummaryCard(
    icon: ImageVector,
    title: String,
    count: String,
    gradient: List<androidx.compose.ui.graphics.Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = TextWhite.copy(alpha = 0.9f),
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        count,
                        style = MaterialTheme.typography.displayMedium,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextWhite.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta de acceso rápido
 */
@Composable
fun QuickAccessCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = OrangeMain,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = TextWhite)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextGray)
        }
    }
}

/**
 * Tarjeta de local destacado (horizontal scroll)
 */
@Composable
fun FeaturedFoodPlaceCard(
    foodPlace: FoodPlace,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Icono de categoría
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(OrangeMain.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (foodPlace.category.name) {
                        "RESTAURANTE" -> "🍽️"
                        "CAFETERIA" -> "☕"
                        "PANADERIA" -> "🥖"
                        "COMIDA_RAPIDA" -> "🍔"
                        "MARISQUERIA" -> "🦐"
                        "PASTELERIA" -> "🎂"
                        "HELADERIA" -> "🍦"
                        else -> "🍴"
                    },
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                foodPlace.name,
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                foodPlace.category.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = OrangeMain
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    String.format("%.1f", foodPlace.rating),
                    style = MaterialTheme.typography.bodySmall,
                    color = GoldAccent
                )
            }
        }
    }
}
