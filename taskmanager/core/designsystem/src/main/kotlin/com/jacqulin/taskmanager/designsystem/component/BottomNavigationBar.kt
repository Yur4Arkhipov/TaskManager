package com.jacqulin.taskmanager.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.model.BottomBarItem

@Composable
fun BottomNavigationBar(
    items: List<BottomBarItem>,
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
