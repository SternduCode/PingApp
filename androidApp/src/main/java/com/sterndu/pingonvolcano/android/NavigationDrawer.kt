package com.sterndu.pingonvolcano.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Drawerheader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 64.dp),
       contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fontFamily = FontFamily(
                Font(R.font.cascadiacode)
            )
            Text(text = "Ping", fontSize = 60.sp,fontFamily = fontFamily)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "~by Sterndu", fontSize = 20.sp,fontFamily = fontFamily)
        }
    }
}

@Composable
fun Drawerbody(
    itemsList: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
){
    LazyColumn(modifier) {
        items(items = itemsList) { item ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
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
