package cn.net.ilean.cms.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipFilter(options: Map<String, String>, onOptionSelected: (String) -> Unit) {
    var currentSelection by remember { mutableStateOf(options.keys.first()) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        for ((key, value) in options) {
            FilterChip(selected = key == currentSelection,
                onClick = { currentSelection = key; onOptionSelected(value) },
                label = { Text(key) })
        }
    }
}