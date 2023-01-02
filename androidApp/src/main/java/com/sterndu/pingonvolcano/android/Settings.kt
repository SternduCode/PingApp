package com.sterndu.pingonvolcano.android

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class Settings {


    @Composable
    fun View() {
        val scrollState = rememberScrollState()
        val fontFamily = FontFamily(
            Font(R.font.cascadiacode)
        )
        ApplicationTheme() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Settings",fontSize = 30.sp, fontFamily = fontFamily)
                Spacer(modifier = Modifier.size(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState)
                        ) {

                }
            }
        }

    }

}
@Preview
@Composable
fun DefaultPreview3() {
    ApplicationTheme {
        Settings().View()
    }
}