package cn.net.ilean.cms

import androidx.navigation.NavHostController

object LeanDestination {
    const val COMPANIES_ROUTE = "companies"
    const val EMPLOYEES_ROUTE = "employees"
}

class LeanNavigationActions(navController: NavHostController) {
    val navigateToEmployees: () -> Unit = {
        navController.navigate(LeanDestination.EMPLOYEES_ROUTE)
    }

    val navigateToCompanies: () -> Unit = {
        navController.navigate(LeanDestination.COMPANIES_ROUTE)
    }
}