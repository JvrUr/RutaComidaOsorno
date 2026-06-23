package com.example.osornogourmet.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
 * Pantalla principal (Dashboard) con diseño elegante
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
                            "¡Hola, $userName! \uD83D\uDC4B",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextWhite,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            "Descubre Osorno",
                            style = MaterialTheme.typography.titleLarge,
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(DarkBackground, DarkSurface, WarmBrown.copy(alpha = 0.4f)),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            // Orbes decorativos
            Box(
                modifier = Modifier
                    .offset(x = 100.dp, y = (-50).dp)
                    .size(250.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(OrangeMain.copy(alpha = 0.15f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Accesos rápidos (Orden invertido y diseño de píldora)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ElegantQuickAccess(
                        icon = Icons.Rounded.Route,
                        title = "Rutas",
                        subtitle = "Explorar",
                        gradient = listOf(GoldAccent, OrangeMain),
                        onClick = onNavigateToRoutes,
                        modifier = Modifier.weight(1f)
                    )
                    ElegantQuickAccess(
                        icon = Icons.Rounded.Map,
                        title = "Mapa",
                        subtitle = "Ubicaciones",
                        gradient = listOf(OrangeMain, OrangeDark),
                        onClick = onNavigateToMap,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Resumen estadístico elegante
                Text(
                    "TU ACTIVIDAD",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextGray,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ElegantSummaryCard(
                        value = foodPlaces.size.toString(),
                        label = "Locales registrados",
                        modifier = Modifier.weight(1f)
                    )
                    ElegantSummaryCard(
                        value = routes.size.toString(),
                        label = "Rutas trazadas",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Locales destacados
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "DESTACADOS",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextGray,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Ver todos",
                        color = GoldAccent,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToFoodPlaces() }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                if (foodPlaces.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .border(1.dp, TextGray.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aún no hay locales", color = TextGray)
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(foodPlaces.take(8)) { place ->
                            ElegantFoodPlaceCard(
                                foodPlace = place,
                                onClick = { onNavigateToFoodPlaceDetail(place.id) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Categorías en chips translúcidos
                Text(
                    "EXPLORAR POR",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextGray,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                val categoryCounts = foodPlaces.groupBy { it.category }.mapValues { it.value.size }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categoryCounts.entries.toList()) { (category, count) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                .clickable { onNavigateToFoodPlaces() }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                "${category.displayName} ($count)",
                                color = TextWhite,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ElegantQuickAccess(
    icon: ImageVector,
    title: String,
    subtitle: String,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(gradient))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    title,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    subtitle,
                    color = TextWhite.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ElegantSummaryCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            value,
            style = MaterialTheme.typography.displaySmall,
            color = TextWhite,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = TextGray
        )
    }
}

@Composable
fun ElegantFoodPlaceCard(
    foodPlace: FoodPlace,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Icono transparente suave
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(OrangeMain.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Restaurant, contentDescription = null, tint = OrangeMain, modifier = Modifier.size(20.dp))
                }
                
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        String.format("%.1f", foodPlace.rating),
                        style = MaterialTheme.typography.labelMedium,
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                foodPlace.name,
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                foodPlace.category.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
        }
    }
}
