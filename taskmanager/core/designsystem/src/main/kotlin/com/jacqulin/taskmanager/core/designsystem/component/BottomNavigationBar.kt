package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    items: List<com.jacqulin.taskmanager.core.designsystem.model.BottomBarItem>,
    modifier: Modifier = Modifier
) {
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.selected,
                onClick = item.onClick,
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.contentDescription,
                        modifier = modifier.size(24.dp)
                    )
                }
            )
        }
    }
}
