package com.example.osornogourmet.presentation.ui.foodplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osornogourmet.presentation.theme.*
import com.example.osornogourmet.presentation.viewmodel.FoodPlaceViewModel

/**
 * Pantalla de detalle de un local de comida
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodPlaceDetailScreen(
    foodPlaceId: Long,
    viewModel: FoodPlaceViewModel,
    onNavigateToEdit: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToMap: () -> Unit
) {
    val foodPlace by viewModel.selectedFoodPlace.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(foodPlaceId) {
        viewModel.loadFoodPlaceById(foodPlaceId)
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog && foodPlace != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Local", color = TextWhite) },
            text = { Text("¿Estás seguro de eliminar '${foodPlace!!.name}'?", color = TextGray) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteFoodPlace(foodPlace!!) {
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = TextGray)
                }
            },
            containerColor = DarkCard
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Local", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextWhite)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToEdit) {
                        Icon(Icons.Default.Edit, "Editar", tint = GoldAccent)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (foodPlace == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = OrangeMain)
            }
        } else {
            val place = foodPlace!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Cabecera con emoji y nombre
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(OrangeMain.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                getCategoryEmoji(place.category),
                                fontSize = 40.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            place.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        AssistChip(
                            onClick = {},
                            label = { Text(place.category.displayName) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = OrangeMain.copy(alpha = 0.2f),
                                labelColor = OrangeMain
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) { index ->
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < place.rating.toInt()) GoldAccent else TextGray.copy(alpha = 0.3f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                String.format("%.1f", place.rating),
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldAccent
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Información detallada
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        DetailRow(
                            icon = Icons.Default.Description,
                            label = "Descripción",
                            value = place.description
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = TextGray.copy(alpha = 0.2f)
                        )
                        DetailRow(
                            icon = Icons.Default.LocationOn,
                            label = "Dirección",
                            value = place.address
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = TextGray.copy(alpha = 0.2f)
                        )
                        DetailRow(
                            icon = Icons.Default.MyLocation,
                            label = "Coordenadas",
                            value = "Lat: ${place.latitude}, Lng: ${place.longitude}"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón ver en mapa
                Button(
                    onClick = onNavigateToMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver en el Mapa", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * Fila de detalle con icono, etiqueta y valor
 */
@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            icon,
            contentDescription = null,
            tint = OrangeMain,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                color = TextWhite
            )
        }
    }
}
