package ru.ragefalcon.cheatsheetandroid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.ragefalcon.cheatsheetandroid.compose.home.HomePage
import ru.ragefalcon.cheatsheetandroid.compose.lifecycles.LifecycleCheatOpenScreen
import ru.ragefalcon.cheatsheetandroid.di.OrientationState
import ru.ragefalcon.cheatsheetandroid.viewmodels.LifecycleCheatViewModel

@Composable
fun CheatSheetApp(orientationState: OrientationState, startDestination: (NavController) -> Unit) {
    val navController = rememberNavController()
    CheatSheetNavHost(navController, orientationState, startDestination)
}

@Composable
fun CheatSheetNavHost(
    navController: NavHostController,
    orientationState: OrientationState,
    startDestination: (NavController) -> Unit
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(orientationState) { cheat ->
                navController.navigate("lifecycleCheat/${cheat.name}")
            }
            LaunchedEffect(Unit) {
                startDestination(navController)
            }
        }
        composable(
            "lifecycleCheat/{${LifecycleCheatViewModel.LIFECYCLE_CHEAT_SAVED_STATE_KEY}}",
            arguments = listOf(navArgument(LifecycleCheatViewModel.LIFECYCLE_CHEAT_SAVED_STATE_KEY) {
                type = NavType.StringType
            })
        ) {
            LifecycleCheatOpenScreen(orientationState = orientationState) {
                navController.navigateUp()
            }
        }
    }
}