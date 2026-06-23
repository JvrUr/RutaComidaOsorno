package com.example.osornogourmet.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.osornogourmet.data.remote.OrsRetrofitClient
import com.example.osornogourmet.domain.usecase.auth.LoginUseCase
import com.example.osornogourmet.domain.usecase.auth.RegisterUseCase
import com.example.osornogourmet.domain.usecase.foodplace.*
import com.example.osornogourmet.domain.usecase.route.*
import com.example.osornogourmet.presentation.viewmodel.*
import com.example.osornogourmet.presentation.ui.auth.LoginScreen
import com.example.osornogourmet.presentation.ui.auth.RegisterScreen
import com.example.osornogourmet.presentation.ui.home.HomeScreen
import com.example.osornogourmet.presentation.ui.foodplace.FoodPlaceListScreen
import com.example.osornogourmet.presentation.ui.foodplace.FoodPlaceDetailScreen
import com.example.osornogourmet.presentation.ui.foodplace.FoodPlaceFormScreen
import com.example.osornogourmet.presentation.ui.route.RouteListScreen
import com.example.osornogourmet.presentation.ui.route.RouteFormScreen
import com.example.osornogourmet.presentation.ui.map.MapScreen

/**
 * Rutas de navegación de la aplicación
 */
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val FOOD_PLACE_LIST = "food_places"
    const val FOOD_PLACE_DETAIL = "food_place_detail/{foodPlaceId}"
    const val FOOD_PLACE_FORM = "food_place_form?foodPlaceId={foodPlaceId}"
    const val ROUTE_LIST = "routes"
    const val ROUTE_FORM = "route_form?routeId={routeId}"
    const val MAP = "map?routeId={routeId}"

    fun foodPlaceDetail(id: Long) = "food_place_detail/$id"
    fun foodPlaceForm(id: Long? = null) = if (id != null) "food_place_form?foodPlaceId=$id" else "food_place_form"
    fun routeForm(id: Long? = null) = if (id != null) "route_form?routeId=$id" else "route_form"
    fun map(routeId: Long? = null) = if (routeId != null) "map?routeId=$routeId" else "map"
}

/**
 * Grafo de navegación principal de OsornoGourmet
 */
@Composable
fun NavGraph() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Inicializar dependencias de Red y Repositorios Remotos
    val tokenManager = com.example.osornogourmet.data.local.TokenManager(context)
    val gourmetApi = com.example.osornogourmet.data.remote.GourmetApiClient.getInstance(tokenManager)

    val userRepo = com.example.osornogourmet.data.repository.RemoteUserRepositoryImpl(gourmetApi, tokenManager)
    val foodPlaceRepo = com.example.osornogourmet.data.repository.RemoteFoodPlaceRepositoryImpl(gourmetApi)
    val routeRepo = com.example.osornogourmet.data.repository.RemoteRouteRepositoryImpl(gourmetApi)

    // UseCases de autenticación
    val loginUseCase = LoginUseCase(userRepo)
    val registerUseCase = RegisterUseCase(userRepo)

    // UseCases de locales
    val getFoodPlacesUseCase = GetFoodPlacesUseCase(foodPlaceRepo)
    val getFoodPlacesByCategoryUseCase = GetFoodPlacesByCategoryUseCase(foodPlaceRepo)
    val getFoodPlaceByIdUseCase = GetFoodPlaceByIdUseCase(foodPlaceRepo)
    val addFoodPlaceUseCase = AddFoodPlaceUseCase(foodPlaceRepo)
    val updateFoodPlaceUseCase = UpdateFoodPlaceUseCase(foodPlaceRepo)
    val deleteFoodPlaceUseCase = DeleteFoodPlaceUseCase(foodPlaceRepo)
    val getFoodPlacesByIdsUseCase = GetFoodPlacesByIdsUseCase(foodPlaceRepo)

    // UseCases de rutas
    val getRoutesUseCase = GetRoutesUseCase(routeRepo)
    val getRoutesByUserUseCase = GetRoutesByUserUseCase(routeRepo)
    val getRouteByIdUseCase = GetRouteByIdUseCase(routeRepo)
    val createRouteUseCase = CreateRouteUseCase(routeRepo)
    val updateRouteUseCase = UpdateRouteUseCase(routeRepo)
    val deleteRouteUseCase = DeleteRouteUseCase(routeRepo)

    // ViewModels (Factory pattern via compose viewModel)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(loginUseCase, registerUseCase)
    )
    val foodPlaceViewModel: FoodPlaceViewModel = viewModel(
        factory = FoodPlaceViewModel.Factory(
            getFoodPlacesUseCase, getFoodPlacesByCategoryUseCase,
            getFoodPlaceByIdUseCase, addFoodPlaceUseCase,
            updateFoodPlaceUseCase, deleteFoodPlaceUseCase
        )
    )
    val routeViewModel: RouteViewModel = viewModel(
        factory = RouteViewModel.Factory(
            getRoutesUseCase, getRoutesByUserUseCase, getRouteByIdUseCase,
            createRouteUseCase, updateRouteUseCase, deleteRouteUseCase,
            getFoodPlacesByIdsUseCase, getFoodPlacesUseCase
        )
    )
    val mapViewModel: MapViewModel = viewModel(
        factory = MapViewModel.Factory(
            getFoodPlacesUseCase, getRoutesUseCase, getRouteByIdUseCase,
            getFoodPlacesByIdsUseCase, OrsRetrofitClient.api
        )
    )

    // Estado de autenticación
    val currentUser by authViewModel.currentUser.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) Routes.HOME else Routes.LOGIN
    ) {
        // Pantalla de Login
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // Pantalla de Registro
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Home (Dashboard)
        composable(Routes.HOME) {
            HomeScreen(
                foodPlaceViewModel = foodPlaceViewModel,
                routeViewModel = routeViewModel,
                userName = currentUser?.name ?: "Usuario",
                onNavigateToFoodPlaces = { navController.navigate(Routes.FOOD_PLACE_LIST) },
                onNavigateToRoutes = { navController.navigate(Routes.ROUTE_LIST) },
                onNavigateToMap = { navController.navigate(Routes.map()) },
                onNavigateToFoodPlaceDetail = { id -> navController.navigate(Routes.foodPlaceDetail(id)) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Lista de Locales de Comida
        composable(Routes.FOOD_PLACE_LIST) {
            FoodPlaceListScreen(
                viewModel = foodPlaceViewModel,
                onNavigateToDetail = { id -> navController.navigate(Routes.foodPlaceDetail(id)) },
                onNavigateToForm = { navController.navigate(Routes.foodPlaceForm()) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Detalle de Local de Comida
        composable(
            route = Routes.FOOD_PLACE_DETAIL,
            arguments = listOf(navArgument("foodPlaceId") { type = NavType.LongType })
        ) { backStackEntry ->
            val foodPlaceId = backStackEntry.arguments?.getLong("foodPlaceId") ?: 0L
            FoodPlaceDetailScreen(
                foodPlaceId = foodPlaceId,
                viewModel = foodPlaceViewModel,
                onNavigateToEdit = { navController.navigate(Routes.foodPlaceForm(foodPlaceId)) },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate(Routes.map()) }
            )
        }

        // Formulario de Local (crear/editar - Open/Closed principle)
        composable(
            route = Routes.FOOD_PLACE_FORM,
            arguments = listOf(navArgument("foodPlaceId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val foodPlaceId = backStackEntry.arguments?.getLong("foodPlaceId") ?: -1L
            FoodPlaceFormScreen(
                foodPlaceId = if (foodPlaceId == -1L) null else foodPlaceId,
                viewModel = foodPlaceViewModel,
                userId = currentUser?.id ?: 0L,
                onSaved = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Lista de Rutas
        composable(Routes.ROUTE_LIST) {
            RouteListScreen(
                viewModel = routeViewModel,
                onNavigateToForm = { navController.navigate(Routes.routeForm()) },
                onNavigateToMap = { routeId -> navController.navigate(Routes.map(routeId)) },
                onNavigateToEditForm = { routeId -> navController.navigate(Routes.routeForm(routeId)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Formulario de Ruta
        composable(
            route = Routes.ROUTE_FORM,
            arguments = listOf(navArgument("routeId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getLong("routeId") ?: -1L
            RouteFormScreen(
                routeId = if (routeId == -1L) null else routeId,
                viewModel = routeViewModel,
                userId = currentUser?.id ?: 0L,
                onSaved = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Mapa con OpenStreetMap
        composable(
            route = Routes.MAP,
            arguments = listOf(navArgument("routeId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getLong("routeId") ?: -1L
            MapScreen(
                routeId = if (routeId == -1L) null else routeId,
                viewModel = mapViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
