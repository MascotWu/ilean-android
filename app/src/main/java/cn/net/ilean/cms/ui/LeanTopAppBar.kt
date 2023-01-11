package cn.net.ilean.cms.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeanTopAppBar(onNavigationIcon: @Composable () -> Unit) {
    TopAppBar(
        title = {
            Text("iLean", color = MaterialTheme.colorScheme.primary)
        },
        navigationIcon = onNavigationIcon,
    )
}