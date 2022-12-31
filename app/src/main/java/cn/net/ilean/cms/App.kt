package cn.net.ilean.cms

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cn.net.ilean.cms.LeanDestination.COMPANIES_ROUTE
import cn.net.ilean.cms.LeanDestination.EMPLOYEES_ROUTE
import cn.net.ilean.cms.network.CompanyService
import cn.net.ilean.cms.network.LoginService
import cn.net.ilean.cms.network.UserService
import cn.net.ilean.cms.ui.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

lateinit var companyService: CompanyService
lateinit var userService: UserService
lateinit var loginService: LoginService

@Composable
fun App() {
    val retrofit = Retrofit.Builder().baseUrl("https://ilean.net.cn/api/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()

    loginService = retrofit.create(LoginService::class.java)
    companyService = retrofit.create(CompanyService::class.java)
    userService = retrofit.create(UserService::class.java)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navigationActions = LeanNavigationActions(navController)
    NavHost(navController = navController, startDestination = COMPANIES_ROUTE) {
        composable("login") {
            Login {
                navController.navigate("overview")
            }
        }
        composable(COMPANIES_ROUTE) {
            Companies(navigationActions) { companyId -> navController.navigate("company/$companyId") }
        }
        composable(EMPLOYEES_ROUTE) {
            Employees(navigationActions)
        }
        composable(
            "company/{companyId}",
            arguments = listOf(navArgument("companyId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            Company(
                companyId = arguments?.getInt("companyId")!!,
                navigationActions = navigationActions
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

