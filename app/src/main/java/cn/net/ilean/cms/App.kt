package cn.net.ilean.cms

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cn.net.ilean.cms.LeanDestination.COMPANIES_ROUTE
import cn.net.ilean.cms.LeanDestination.EMPLOYEES_ROUTE
import cn.net.ilean.cms.ui.*
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LeanApplication : Application()

@Composable
fun App() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: LeanDestination.COMPANIES_ROUTE
    val navigationActions = LeanNavigationActions(navController)
    val mainViewModel = hiltViewModel<MainViewModel>()
    NavHost(navController = navController, startDestination = COMPANIES_ROUTE) {
        composable("login") {
            Login {
                navController.navigate("overview")
            }
        }
        composable(COMPANIES_ROUTE) {
            Companies(
                navigationActions, mainViewModel, currentRoute
            ) { companyId -> navController.navigate("company/$companyId") }
        }
        composable(EMPLOYEES_ROUTE) {
            Employees(navigationActions, mainViewModel, currentRoute)
        }
        composable(
            "company/{companyId}", arguments = listOf(navArgument("companyId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            Company(
                companyId = arguments?.getInt("companyId")!!,
                navigationActions = navigationActions,
                mainViewModel = mainViewModel,
            ) { navController.popBackStack() }
        }
        composable("profile/{userId}") {
            Profile { navController.navigate("friends/jia/43") }
        }
        composable(
            "friends/{name}/{id}", arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("id") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val id = arguments?.getInt("id")
            val name = arguments?.getString("name")
            Friends(id, name)
        }
    }
}

