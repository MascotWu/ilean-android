package cn.net.ilean.cms.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun LeanTopAppBar(onNavigationIcon: @Composable () -> Unit) {
    TopAppBar(
        title = {
            Text("iLean")
        },
        navigationIcon = onNavigationIcon,
    )
}