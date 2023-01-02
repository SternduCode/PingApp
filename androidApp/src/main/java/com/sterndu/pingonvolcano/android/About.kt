package com.sterndu.pingonvolcano.android

import androidx.compose.foundation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class About {

    @OptIn(ExperimentalTextApi::class)
    @Composable
    fun View() {
        val scrollState = rememberScrollState()
        val fontFamily = FontFamily(
            Font(R.font.cascadiacode)
        )
        ApplicationTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        .verticalScroll(state = scrollState)
                ) {
                    val abouttext = stringResource(R.string.about_text)
                    val abouttext2 = stringResource(R.string.about_text2)
                    val description = stringResource(R.string.description)
                    val gradientColors = listOf(
                        Color(0xc81e22e2),
                        Color(0xc81485ec),
                        Color(0xc81be4e5),
                        Color(0xc884f27c),
                        Color(0xc8d5dd2b),
                        Color(0xc8e79e18),
                        Color(0xc8d93027),
                        Color(0xc88f2971)
                    )
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(id = R.drawable.sterndu_profile), contentDescription = null, modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)

                        )
                        Column(modifier = Modifier.padding(start = 18.dp)) {
                            Text(
                                text = "Sterndu",
                                fontSize = 25.sp
                            )
                            Text(text = "aka SternduCode")
                        }
                    }
                    SelectionContainer {
                        Text(text = abouttext,
                            style = TextStyle(
                                Brush.linearGradient(
                                    colors = gradientColors
                                )
                            ),
                            modifier = Modifier.padding(9.dp),
                            fontSize = 16.sp)

                    }
                    Divider(thickness = 2.dp)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)) {
                        Text(text = "A little bit of graphical design:")
                    }
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(id = R.drawable.cat__186), contentDescription = null, modifier = Modifier
                            .size(80.dp)
                        )
                        Column(modifier = Modifier.padding(start = 18.dp)) {
                            Text(
                                text = "Felix",
                                fontSize = 25.sp
                            )
                            Text(text = "aka felixcrafter54")
                        }
                    }
                    SelectionContainer {
                        Text(text = abouttext2,
                            style = TextStyle(
                                Brush.linearGradient(
                                    colors = gradientColors
                                )
                            ),
                            modifier = Modifier.padding(9.dp),
                            fontSize = 16.sp)

                    }
                    Divider(thickness = 2.dp)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        val uriHandler = LocalUriHandler.current
                        Image(painterResource(id = if (isSystemInDarkTheme()) R.drawable.github_mark_white else R.drawable.github_mark), contentDescription = null, modifier = Modifier
                            .size(60.dp)
                            .clickable { uriHandler.openUri("https://github.com/SternduCode") }
                        )
                        val annotatedString = buildAnnotatedString {
                            pushStringAnnotation(
                                tag = "SternduCode",
                                annotation = "https://github.com/SternduCode"
                            )
                            append("SternduCode")
                            pop()
                        }

                        ClickableText(
                            text = annotatedString,
                            onClick = {
                                    offset -> annotatedString
                                .getStringAnnotations(tag = "SternduCode", start = offset,end = offset)
                                .firstOrNull()?.let { stringAnnotation ->
                                    uriHandler.openUri(stringAnnotation.item)
                                }
                            }
                        )
                    }
                    Divider(thickness = 2.dp, modifier = Modifier.padding(top = 5.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)) {
                        Text(text = "Description of the App:",fontFamily = fontFamily)
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()) {
                        Text(text = description,modifier = Modifier.padding(9.dp),fontFamily = fontFamily)
                        Column(modifier = Modifier
                            .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Row (modifier = Modifier.padding(top = 5.dp),verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painterResource(id = if (isSystemInDarkTheme()) R.drawable.ic_baseline_copyright_24_white else R.drawable.ic_baseline_copyright_24),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                                Text(text = "2023",Modifier.padding(start = 5.dp),fontFamily = fontFamily)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview2() {
    ApplicationTheme {
        About().View()
    }
}