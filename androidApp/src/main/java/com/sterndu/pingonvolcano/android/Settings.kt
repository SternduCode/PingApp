package com.sterndu.pingonvolcano.android

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sterndu.pingonvolcano.AppViewModel
import kotlinx.serialization.json.JsonPrimitive


class Settings {

	@Composable
	fun View(appViewModel: AppViewModel) {
		val scrollState = rememberScrollState()
		val fontFamily = FontFamily(
			Font(R.font.cascadiacode)
		)
		ApplicationTheme {
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
					Text(text = "Display", modifier = Modifier
						.padding(start = 16.dp)
						.alpha(0.8f)
					)

					var design: JsonPrimitive by remember {
						mutableStateOf(
							(appViewModel.preferences["design"] as? JsonPrimitive)?: run {
								appViewModel.preferences["design"] = JsonPrimitive("System default")
								JsonPrimitive("System default")
							}
						)
					}

					var expanded by remember { mutableStateOf(false) }
					Column( //to click Design
						modifier = Modifier
							.fillMaxSize()
							.clickable { expanded = true }
					) {
						DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
							DropdownMenuItem(onClick = {
								design =  JsonPrimitive("System default")
								with(appViewModel) { appViewModel.preferences["design"] = design }
								expanded = false
							}) {
								Text(text = "System default")
							}
							DropdownMenuItem(onClick = {
								design = JsonPrimitive("White")
								with(appViewModel) { appViewModel.preferences["design"] = design }
								expanded = false
							}) {
								Text(text = "White")
							}
							DropdownMenuItem(onClick = {
								design = JsonPrimitive("Black")
								with(appViewModel) { appViewModel.preferences["design"] = design }
								expanded = false
							}) {
								Text(text = "Black")
							}
						}
						Column(
							modifier = Modifier
								.fillMaxSize()
								.padding(16.dp)
						) {
							Row(
								modifier = Modifier
									.fillMaxSize(),
								verticalAlignment = Alignment.CenterVertically
							) {
								Image(
									painterResource(id = if (isSystemInDarkTheme()) R.drawable.baseline_light_mode_24_white else R.drawable.baseline_light_mode_24), contentDescription = null, modifier = Modifier
										.size(35.dp)
								)
								Spacer(modifier = Modifier.size(16.dp))
								Column(
									modifier = Modifier
										.fillMaxSize()
								) {
									Text(text = "Design", fontSize = 18.sp)
									Text(text = design.toString().substring(1, design.toString().length-1), fontSize = 16.sp,
										modifier = Modifier
											.alpha(0.8f)
									)

								}
							}
						}


					}
					Divider(thickness = 1.dp, modifier = Modifier
						.padding(top = 5.dp))
					Text(text = "Chat-Settings", modifier = Modifier
						.padding(start = 16.dp, top = 20.dp)
						.alpha(0.8f)
					)
					var checked by remember { mutableStateOf(false) }
					Column( //to click Enter -> Send
						modifier = Modifier
							.fillMaxSize()
							.clickable { checked = !checked }
					) {
						Column(
							modifier = Modifier
								.fillMaxSize()
								.padding(16.dp)
						) {
							Row(
								modifier = Modifier
									.fillMaxSize(),
								verticalAlignment = Alignment.CenterVertically
							) {
								Spacer(modifier = Modifier.size(35.dp)) //Picture
								Spacer(modifier = Modifier.size(16.dp))
								Column(
									modifier = Modifier
										.fillMaxSize()
										.weight(1f)
								) {
									Text(text = "Enter -> Sent", fontSize = 18.sp)
									Text(text = "Enter sent´s message", fontSize = 16.sp,
										modifier = Modifier
											.alpha(0.8f)
									)

								}
								Switch(
									checked = checked,
									onCheckedChange = { checked = !checked }
								)
							}
						}
					}
					var size by remember { mutableStateOf("normal") }
					var expanded2 by remember { mutableStateOf(false) }
					Column( //font size
						modifier = Modifier
							.fillMaxSize()
							.clickable { expanded2 = true }
					) {
						DropdownMenu(expanded = expanded2, onDismissRequest = { expanded2 = false}) {
							DropdownMenuItem(onClick = { size = "little"
								expanded2 = false
							}) {
								Text(text = "little")
							}
							DropdownMenuItem(onClick = { size = "normal"
								expanded2 = false
							}) {
								Text(text = "normal")
							}
							DropdownMenuItem(onClick = { size = "big"
								expanded2 = false
							}) {
								Text(text = "big")
							}
						}
						Column(
							modifier = Modifier
								.fillMaxSize()
								.padding(16.dp)
						) {
							Row(
								modifier = Modifier
									.fillMaxSize(),
								verticalAlignment = Alignment.CenterVertically
							) {
								Spacer(modifier = Modifier.size(35.dp)) //Picture
								Spacer(modifier = Modifier.size(16.dp))
								Column(
									modifier = Modifier
										.fillMaxSize()
										.weight(1f)
								) {
									Text(text = "Font-size", fontSize = 18.sp)
									Text(text = size, fontSize = 16.sp,
										modifier = Modifier
											.alpha(0.8f)
									)
								}
							}
						}
					}
					Column(
						modifier = Modifier
							.fillMaxSize(),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						var mCheckedState by remember{ mutableStateOf(false) }
						Switch(
							checked = mCheckedState,
							onCheckedChange = { mCheckedState = it })
					}

				}
			}
		}

	}

}
@Preview
@Composable
fun DefaultPreview3() {
	ApplicationTheme {
		Settings().View(viewModel())
	}
}