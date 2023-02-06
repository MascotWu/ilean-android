package cn.net.ilean.cms.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.net.ilean.cms.LeanDestination
import cn.net.ilean.cms.R
import cn.net.ilean.cms.ui.theme.LeanApplicationTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(currentRoute: String, onDrawerItemSelected: (item: String) -> Unit) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Home, null, tint = MaterialTheme.colorScheme.primary) },
            label = {
                Text(stringResource(R.string.companies))
                FilterChipDefaults.filterChipColors()
                ButtonDefaults.buttonColors()
            },
            selected = currentRoute == LeanDestination.COMPANIES_ROUTE,
            onClick = { onDrawerItemSelected(LeanDestination.COMPANIES_ROUTE) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Filled.AccountBox, null
                )
            },
            label = { Text(stringResource(R.string.users)) },
            selected = currentRoute == LeanDestination.EMPLOYEES_ROUTE,
            onClick = { onDrawerItemSelected(LeanDestination.EMPLOYEES_ROUTE) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewLeanDrawer() {
    LeanApplicationTheme {
        AppDrawer(currentRoute = LeanDestination.EMPLOYEES_ROUTE, onDrawerItemSelected = {})
    }
}