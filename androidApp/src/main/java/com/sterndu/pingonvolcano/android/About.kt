package com.sterndu.pingonvolcano.android

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class About : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            view()

        }
    }

    @Composable
    fun view() {
        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()

        MyApplicationTheme {
            Box(contentAlignment = Alignment.TopEnd) {
                Column(horizontalAlignment = Alignment.End) {
                    MyApplicationTheme2 {
                        Button(
                            onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } },
                            modifier = Modifier
                                .width(Dp(64f))
                                .height(Dp(64f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                modifier = Modifier.size(42.dp),
                                tint = LocalContentColor.current.copy(alpha = .8f)
                            )
                        }

                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val fontFamily = FontFamily(
                        Font(R.font.cascadiacode)
                    )
                    Text(text = "About",fontSize = 30.sp, fontFamily = fontFamily)
                    Spacer(modifier = Modifier.size(20.dp))
                    Column(
                        modifier = Modifier
                            .border(
                                2.dp,
                                TextFieldDefaults
                                    .textFieldColors()
                                    .textColor(
                                        enabled = true
                                    ).value
                            )
                            .fillMaxSize()
                    ) {
                        val abouttext = stringResource(R.string.about_text)
                        Text(text = abouttext, modifier = Modifier.padding(9.dp))
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview2() {
    MyApplicationTheme {
        About().view()
    }
}