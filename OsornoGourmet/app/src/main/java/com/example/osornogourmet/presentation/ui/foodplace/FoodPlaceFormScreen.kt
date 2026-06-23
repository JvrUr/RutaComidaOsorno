package com.example.osornogourmet.presentation.ui.foodplace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.presentation.theme.*
import com.example.osornogourmet.presentation.viewmodel.FoodPlaceViewModel

/**
 * Formulario reutilizable para crear y editar locales (Open/Closed Principle)
 * Se usa el mismo composable para ambas operaciones sin modificación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodPlaceFormScreen(
    foodPlaceId: Long?,
    viewModel: FoodPlaceViewModel,
    userId: Long,
    onSaved: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val isEditing = foodPlaceId != null
    val existingPlace by viewModel.selectedFoodPlace.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(FoodCategory.RESTAURANTE) }
    var address by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("-40.5740") }
    var longitude by remember { mutableStateOf("-73.1340") }
    var rating by remember { mutableStateOf("4.0") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var initialized by remember { mutableStateOf(false) }

    // Cargar datos si estamos editando
    LaunchedEffect(foodPlaceId) {
        if (foodPlaceId != null) {
            viewModel.loadFoodPlaceById(foodPlaceId)
        }
    }

    // Pre-llenar campos al editar
    LaunchedEffect(existingPlace) {
        if (isEditing && existingPlace != null && !initialized) {
            existingPlace!!.let { place ->
                name = place.name
                description = place.description
                category = place.category
                address = place.address
                latitude = place.latitude.toString()
                longitude = place.longitude.toString()
                rating = place.rating.toString()
            }
            initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar Local" else "Nuevo Local",
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre del local") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Descripción
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Categoría (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = category.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            FoodCategory.entries.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text("${getCategoryEmoji(cat)} ${cat.displayName}") },
                                    onClick = {
                                        category = cat
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dirección
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Dirección") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Coordenadas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = latitude,
                            onValueChange = { latitude = it },
                            label = { Text("Latitud") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = longitude,
                            onValueChange = { longitude = it },
                            label = { Text("Longitud") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Rating
                    OutlinedTextField(
                        value = rating,
                        onValueChange = { rating = it },
                        label = { Text("Rating (0-5)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Error
                    if (error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón guardar
                    Button(
                        onClick = {
                            error = null
                            when {
                                name.isBlank() -> error = "Ingresa el nombre del local"
                                description.isBlank() -> error = "Ingresa una descripción"
                                address.isBlank() -> error = "Ingresa la dirección"
                                latitude.toDoubleOrNull() == null -> error = "Latitud inválida"
                                longitude.toDoubleOrNull() == null -> error = "Longitud inválida"
                                rating.toFloatOrNull() == null || rating.toFloat() !in 0f..5f -> error = "Rating debe ser entre 0 y 5"
                                else -> {
                                    val foodPlace = FoodPlace(
                                        id = foodPlaceId ?: 0L,
                                        name = name.trim(),
                                        description = description.trim(),
                                        category = category,
                                        address = address.trim(),
                                        latitude = latitude.toDouble(),
                                        longitude = longitude.toDouble(),
                                        rating = rating.toFloat(),
                                        createdByUserId = userId
                                    )
                                    if (isEditing) {
                                        viewModel.updateFoodPlace(foodPlace, onSaved)
                                    } else {
                                        viewModel.addFoodPlace(foodPlace, onSaved)
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
                            if (isEditing) "Guardar Cambios" else "Crear Local",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
