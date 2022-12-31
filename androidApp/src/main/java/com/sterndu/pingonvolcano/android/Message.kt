package com.sterndu.pingonvolcano.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.sterndu.pingonvolcano.Type

class Message(private val text : String, val type: Type) {

    @Composable
    fun compose(fontSize: TextUnit = TextUnit.Unspecified) {
        val color = TextFieldDefaults.textFieldColors().backgroundColor(enabled = true).value
        Row(
            modifier = Modifier.background(color)
        ) {
            TextField(
                value = text,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dp(type.leftPad.toFloat()), end = Dp(type.rightPad.toFloat())),
                textStyle = TextStyle(fontSize = fontSize),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0f, 0f, 0f, 0f))
            )
        }
    }

}