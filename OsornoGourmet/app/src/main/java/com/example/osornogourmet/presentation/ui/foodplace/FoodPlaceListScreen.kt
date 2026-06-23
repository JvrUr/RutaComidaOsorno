package com.example.osornogourmet.presentation.ui.foodplace

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.presentation.theme.*
import com.example.osornogourmet.presentation.viewmodel.FoodPlaceViewModel

/**
 * Pantalla de lista de locales de comida con filtro por categoría
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodPlaceListScreen(
    viewModel: FoodPlaceViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToForm: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val foodPlaces by viewModel.foodPlaces.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locales de Comida", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToForm,
                containerColor = OrangeMain,
                contentColor = TextWhite
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar local")
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros por categoría
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Chip "Todos"
                item {
                    val isSelected = selectedCategory == null
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.filterByCategory(null) },
                        label = { Text("Todos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OrangeMain,
                            selectedLabelColor = TextWhite,
                            containerColor = DarkCard,
                            labelColor = TextGray
                        )
                    )
                }
                items(FoodCategory.entries.toList()) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.filterByCategory(category) },
                        label = { Text(category.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OrangeMain,
                            selectedLabelColor = TextWhite,
                            containerColor = DarkCard,
                            labelColor = TextGray
                        )
                    )
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = OrangeMain)
                }
            } else if (foodPlaces.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🍽️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay locales", color = TextGray, style = MaterialTheme.typography.titleMedium)
                        Text("Agrega uno con el botón +", color = TextGray, style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(foodPlaces, key = { it.id }) { place ->
                        FoodPlaceCard(
                            foodPlace = place,
                            onClick = { onNavigateToDetail(place.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tarjeta de local de comida en la lista
 */
@Composable
fun FoodPlaceCard(
    foodPlace: FoodPlace,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono categoría
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(OrangeMain.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getCategoryEmoji(foodPlace.category),
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    foodPlace.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextWhite,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    foodPlace.category.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = OrangeMain
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        foodPlace.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Rating
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    String.format("%.1f", foodPlace.rating),
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldAccent
                )
            }
        }
    }
}

/**
 * Obtener emoji por categoría
 */
fun getCategoryEmoji(category: FoodCategory): String {
    return when (category) {
        FoodCategory.RESTAURANTE -> "🍽️"
        FoodCategory.CAFETERIA -> "☕"
        FoodCategory.PANADERIA -> "🥖"
        FoodCategory.COMIDA_RAPIDA -> "🍔"
        FoodCategory.MARISQUERIA -> "🦐"
        FoodCategory.PASTELERIA -> "🎂"
        FoodCategory.HELADERIA -> "🍦"
    }
}
