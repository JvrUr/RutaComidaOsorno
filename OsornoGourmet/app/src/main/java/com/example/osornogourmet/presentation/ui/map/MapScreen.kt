package com.example.osornogourmet.presentation.ui.map

import android.graphics.Color as AndroidColor
import android.preference.PreferenceManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.osornogourmet.presentation.theme.*
import com.example.osornogourmet.presentation.viewmodel.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

/**
 * Pantalla de mapa con OpenStreetMap (osmdroid) - 100% GRATUITO
 * Muestra marcadores de locales y polylines de rutas trazadas con OpenRouteService
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    routeId: Long?,
    viewModel: MapViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Configurar osmdroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = "OsornoGourmet/1.0"
    }

    // Cargar ruta si se proporcionó ID
    LaunchedEffect(routeId) {
        if (routeId != null) {
            viewModel.selectRoute(routeId)
        }
    }

    // Dropdown de rutas
    var routeDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de Osorno", color = TextWhite) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mapa OpenStreetMap
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(14.5)
                        // Centro de Osorno, Chile
                        controller.setCenter(GeoPoint(-40.5740, -73.1340))
                    }
                },
                update = { mapView ->
                    // Limpiar overlays previos
                    mapView.overlays.clear()

                    // Agregar marcadores de todos los locales
                    uiState.allFoodPlaces.forEach { place ->
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(place.latitude, place.longitude)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = place.name
                        marker.snippet = "${place.category.displayName} - ${place.address}"
                        marker.subDescription = "⭐ ${String.format("%.1f", place.rating)}"

                        // Resaltar marcadores de la ruta seleccionada
                        val isInRoute = uiState.routeFoodPlaces.any { it.id == place.id }
                        if (isInRoute) {
                            // Marcador más prominente para los de la ruta
                            marker.title = "📍 ${place.name}"
                        }

                        mapView.overlays.add(marker)
                    }

                    // Agregar polyline de la ruta
                    if (uiState.routePolyline.isNotEmpty()) {
                        val polyline = Polyline()
                        polyline.outlinePaint.color = AndroidColor.rgb(232, 93, 4) // OrangeMain
                        polyline.outlinePaint.strokeWidth = 8f
                        polyline.setPoints(
                            uiState.routePolyline.map { (lat, lng) ->
                                GeoPoint(lat, lng)
                            }
                        )
                        mapView.overlays.add(polyline)

                        // Ajustar vista para mostrar toda la ruta
                        if (uiState.routeFoodPlaces.isNotEmpty()) {
                            val bounds = org.osmdroid.util.BoundingBox.fromGeoPoints(
                                uiState.routeFoodPlaces.map { GeoPoint(it.latitude, it.longitude) }
                            )
                            mapView.zoomToBoundingBox(bounds.increaseByScale(1.3f), true)
                        }
                    }

                    mapView.invalidate()
                }
            )

            // Panel superior con selector de ruta
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard.copy(alpha = 0.95f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Selector de ruta
                        ExposedDropdownMenuBox(
                            expanded = routeDropdownExpanded,
                            onExpandedChange = { routeDropdownExpanded = !routeDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = uiState.selectedRoute?.name ?: "Seleccionar ruta...",
                                onValueChange = {},
                                readOnly = true,
                                leadingIcon = { Icon(Icons.Default.Route, null, tint = OrangeMain) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = routeDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = MaterialTheme.typography.bodyMedium
                            )
                            ExposedDropdownMenu(
                                expanded = routeDropdownExpanded,
                                onDismissRequest = { routeDropdownExpanded = false }
                            ) {
                                // Opción para limpiar
                                DropdownMenuItem(
                                    text = { Text("Sin ruta (mostrar todos)") },
                                    onClick = {
                                        viewModel.clearRoute()
                                        routeDropdownExpanded = false
                                    }
                                )
                                HorizontalDivider()
                                uiState.routes.forEach { route ->
                                    DropdownMenuItem(
                                        text = { Text("${route.name} (${route.foodPlaceIds.size} locales)") },
                                        onClick = {
                                            viewModel.selectRoute(route.id)
                                            routeDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Info de la ruta seleccionada
                        if (uiState.selectedRoute != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                if (uiState.isLoadingRoute) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = OrangeMain,
                                        strokeWidth = 2.dp
                                    )
                                    Text("Trazando ruta...", color = TextGray, style = MaterialTheme.typography.bodySmall)
                                } else {
                                    if (uiState.routeDistance.isNotEmpty()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Straighten, null, tint = OrangeMain, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(uiState.routeDistance, style = MaterialTheme.typography.bodySmall, color = TextWhite)
                                        }
                                    }
                                    if (uiState.routeDuration.isNotEmpty()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Schedule, null, tint = OrangeMain, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(uiState.routeDuration, style = MaterialTheme.typography.bodySmall, color = TextWhite)
                                        }
                                    }
                                    Text(
                                        "${uiState.routeFoodPlaces.size} locales",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OrangeMain
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Error snackbar
            if (uiState.error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK", color = OrangeMain)
                        }
                    },
                    containerColor = DarkCard
                ) {
                    Text(uiState.error!!, color = TextWhite, style = MaterialTheme.typography.bodySmall)
                }
            }

            // Atribución de OpenStreetMap
            Text(
                "© OpenStreetMap contributors | © OpenRouteService",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(DarkCard.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .padding(4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = TextGray
            )
        }
    }
}
