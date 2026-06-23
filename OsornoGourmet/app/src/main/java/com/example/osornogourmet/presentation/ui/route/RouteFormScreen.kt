package com.example.osornogourmet.presentation.ui.route

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.presentation.theme.*
import com.example.osornogourmet.presentation.ui.foodplace.getCategoryEmoji
import com.example.osornogourmet.presentation.viewmodel.RouteViewModel

/**
 * Formulario para crear/editar rutas gastronómicas
 * Permite seleccionar locales y configurar la ruta
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteFormScreen(
    routeId: Long?,
    viewModel: RouteViewModel,
    userId: Long,
    onSaved: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val isEditing = routeId != null
    val allFoodPlaces by viewModel.allFoodPlaces.collectAsState()
    val selectedRoute by viewModel.selectedRoute.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPlaceIds by remember { mutableStateOf<Set<Long>>(emptySet()) }
    var error by remember { mutableStateOf<String?>(null) }
    var initialized by remember { mutableStateOf(false) }

    // Cargar ruta existente si estamos editando
    LaunchedEffect(routeId) {
        if (routeId != null) {
            viewModel.loadRouteById(routeId)
        }
    }

    // Pre-llenar campos
    LaunchedEffect(selectedRoute) {
        if (isEditing && selectedRoute != null && !initialized) {
            selectedRoute!!.let { route ->
                name = route.name
                description = route.description
                selectedPlaceIds = route.foodPlaceIds.toSet()
            }
            initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar Ruta" else "Nueva Ruta",
                        color = TextWhite
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Campos de nombre y descripción
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre de la ruta") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        minLines = 2,
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selección de locales
            Text(
                "Selecciona los locales (${selectedPlaceIds.size} seleccionados)",
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Error
            if (error != null) {
                Text(
                    error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista de locales con checkbox
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allFoodPlaces, key = { it.id }) { place ->
                    val isSelected = place.id in selectedPlaceIds
                    FoodPlaceCheckItem(
                        foodPlace = place,
                        isSelected = isSelected,
                        onToggle = {
                            selectedPlaceIds = if (isSelected) {
                                selectedPlaceIds - place.id
                            } else {
                                selectedPlaceIds + place.id
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón guardar
            Button(
                onClick = {
                    error = null
                    when {
                        name.isBlank() -> error = "Ingresa el nombre de la ruta"
                        description.isBlank() -> error = "Ingresa una descripción"
                        selectedPlaceIds.size < 2 -> error = "Selecciona al menos 2 locales"
                        else -> {
                            val route = Route(
                                id = routeId ?: 0L,
                                name = name.trim(),
                                description = description.trim(),
                                foodPlaceIds = selectedPlaceIds.toList(),
                                createdByUserId = userId
                            )
                            if (isEditing) {
                                viewModel.updateRoute(route, onSaved)
                            } else {
                                viewModel.createRoute(route, onSaved)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
            ) {
                Text(
                    if (isEditing) "Guardar Cambios" else "Crear Ruta",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Item de local con checkbox para selección
 */
@Composable
fun FoodPlaceCheckItem(
    foodPlace: FoodPlace,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) OrangeMain.copy(alpha = 0.1f) else DarkCard
        ),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (isSelected) OrangeMain else TextGray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                getCategoryEmoji(foodPlace.category),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    foodPlace.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    foodPlace.category.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) OrangeMain else TextGray
                )
            }
        }
    }
}
