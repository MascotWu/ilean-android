package cn.net.ilean.cms.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import cn.net.ilean.cms.LeanDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeanDrawer(currentRoute: String, onDrawerItemSelected: (item: String) -> Unit) {
    Column {
        NavigationDrawerItem(
            label = { Text("Companies") },
            selected = currentRoute == LeanDestination.COMPANIES_ROUTE,
            onClick = { onDrawerItemSelected(LeanDestination.COMPANIES_ROUTE) })
        NavigationDrawerItem(
            label = { Text("Employees") },
            selected = currentRoute == LeanDestination.EMPLOYEES_ROUTE,
            onClick = { onDrawerItemSelected(LeanDestination.EMPLOYEES_ROUTE) })
    }
}