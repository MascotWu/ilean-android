package cn.net.ilean.cms

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cn.net.ilean.cms.network.CompanyService
import cn.net.ilean.cms.network.LoginService
import cn.net.ilean.cms.ui.Companies
import cn.net.ilean.cms.ui.Company
import cn.net.ilean.cms.ui.Login
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

lateinit var companyService: CompanyService
lateinit var loginService: LoginService

@Composable
fun App() {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://ilean.net.cn/api/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    loginService = retrofit.create(LoginService::class.java)
    companyService = retrofit.create(CompanyService::class.java)
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "overview") {
        composable("login") {
            Login {
                navController.navigate("overview")
            }
        }
        composable("overview") {
            Companies { companyId -> navController.navigate("company/$companyId") }
        }
        composable("company/{companyId}", arguments = listOf(
            navArgument("companyId") { type = NavType.IntType }
        )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            Company(companyId = arguments?.getInt("companyId")!!) { navController.popBackStack() }
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

