package com.sterndu.pingonvolcano.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Drawerheader(drawerWidth: Dp){
    println(drawerWidth)
    Box(modifier = Modifier
        .width(drawerWidth)
        .padding(vertical = 60.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Ping", fontSize = 60.sp)
    }
}

@Composable
fun Drawerbody(
    itemsList: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit,
    drawerWidth: Dp
){
    LazyColumn(modifier) {
        items(items = itemsList) { item ->
            Row (
                modifier = Modifier
                    .width(drawerWidth)
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
                    ){
                Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                modifier = modifier.weight(1f))
            }
        }
    }
}
