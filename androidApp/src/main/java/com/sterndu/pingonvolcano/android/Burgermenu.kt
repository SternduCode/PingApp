package com.sterndu.pingonvolcano.android

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BurgerMenu(openMenu: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Burger Menu") },
                actions = {
                    IconButton(onClick = openMenu) {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_menu_24), contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            MenuItem(text = "Home", icon = painterResource(id = R.drawable.ic_baseline_home_24))
            MenuItem(text = "Settings", icon = painterResource(id = R.drawable.ic_baseline_settings_24))
            MenuItem(text = "Help", icon = painterResource(id = R.drawable.ic_baseline_help_24))
        }
    }
}

@Composable
fun MenuItem(text: String, icon: Painter) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Icon(icon, null,modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.body2)
    }
}