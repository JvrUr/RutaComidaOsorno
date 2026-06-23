package com.example.osornogourmet.presentation.ui.route

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.presentation.theme.*
import com.example.osornogourmet.presentation.viewmodel.RouteViewModel

/**
 * Pantalla de lista de rutas gastronómicas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteListScreen(
    viewModel: RouteViewModel,
    onNavigateToForm: () -> Unit,
    onNavigateToMap: (Long) -> Unit,
    onNavigateToEditForm: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val routes by viewModel.routes.collectAsState()
    var routeToDelete by remember { mutableStateOf<Route?>(null) }

    // Diálogo para eliminar
    if (routeToDelete != null) {
        AlertDialog(
            onDismissRequest = { routeToDelete = null },
            title = { Text("Eliminar Ruta", color = TextWhite) },
            text = { Text("¿Eliminar '${routeToDelete!!.name}'?", color = TextGray) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteRoute(routeToDelete!!)
                    routeToDelete = null
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { routeToDelete = null }) {
                    Text("Cancelar", color = TextGray)
                }
            },
            containerColor = DarkCard
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rutas Gastronómicas", color = TextWhite) },
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
                Icon(Icons.Default.Add, contentDescription = "Crear ruta")
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (routes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🗺️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No hay rutas creadas", color = TextGray, style = MaterialTheme.typography.titleMedium)
                    Text("Crea una ruta con el botón +", color = TextGray, style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(routes, key = { it.id }) { route ->
                    RouteCard(
                        route = route,
                        onViewMap = { onNavigateToMap(route.id) },
                        onEdit = { onNavigateToEditForm(route.id) },
                        onDelete = { routeToDelete = route }
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta de ruta gastronómica
 */
@Composable
fun RouteCard(
    route: Route,
    onViewMap: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        route.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        route.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, "Editar", tint = GoldAccent, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Info de la ruta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Restaurant, null, tint = OrangeMain, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${route.foodPlaceIds.size} locales",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
                if (route.estimatedDistance.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Straighten, null, tint = OrangeMain, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(route.estimatedDistance, style = MaterialTheme.typography.bodySmall, color = TextGray)
                    }
                }
                if (route.estimatedDuration.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = OrangeMain, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(route.estimatedDuration, style = MaterialTheme.typography.bodySmall, color = TextGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón ver en mapa
            OutlinedButton(
                onClick = onViewMap,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = OrangeMain)
            ) {
                Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver en Mapa")
            }
        }
    }
}
