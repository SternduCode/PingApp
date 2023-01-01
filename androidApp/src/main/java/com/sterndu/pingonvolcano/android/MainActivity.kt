package com.sterndu.pingonvolcano.android

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.LinkAddress
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.sterndu.pingonvolcano.*
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		System.setProperty("debug","true")
		start()
		val listLink = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			dos()
		} else null
		val listGlobal = getIps()
		setContent {
			MyApplicationTheme {


				View(listLink, listGlobal)
			}
		}
	}

	@RequiresApi(Build.VERSION_CODES.M)
	fun dos() : List<LinkAddress>? {
		val connectivityManager = getSystemService(ConnectivityManager::class.java)
		val currentNetwork = connectivityManager.activeNetwork

		val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
		val linkProperties = connectivityManager.getLinkProperties(currentNetwork)
		// println(caps)
		// println(linkProperties)

		return linkProperties?.linkAddresses
	}

	override fun onDestroy() {
		super.onDestroy()
		cleanup()
	}

	private var size: Int = 0

	@Composable
	@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
	fun View(listLink: List<LinkAddress>?, listGlobal: List<String>) {
		var localIps: String = ""
		localIps += listLink?.joinToString(separator = "\n  ", prefix = "  ") { "$it" }
		val globalIps: String = "Your Public IPs are:\n${
			listGlobal.joinToString(
				separator = "\n  ",
				prefix = "  "
			) { "$it" }
		}"

		val  appendixList : MutableList<Message> = remember { mutableStateListOf() }

		var listSize: Int = 0

		val textSize: TextUnit = 12f.sp

		val coroutineScope = rememberCoroutineScope()

		val scaffoldState = rememberScaffoldState()

		var input by remember { mutableStateOf("") }
		var output by remember { mutableStateOf("") }

		val listState = rememberLazyListState()

		val view = LocalView.current

		setConsumer({ inp ->
			output = "$inp\nYour Local IPs are:\n$localIps\n$globalIps"
		}) { text, type ->
			runOnUiThread {
				appendixList.add(Message(text, type))
			}
		}
		Scaffold (
			drawerContent = {
				Drawerheader()
				Drawerbody(
					itemsList = listOf(
						MenuItem(
							id = "home",
							title = "Home",
							contentDescription = "Go to home screen",
							icon = Icons.Default.Home
						),
						MenuItem(
							id = "settings",
							title = "Settings",
							contentDescription = "Go to settings",
							icon = Icons.Default.Settings
						),
						MenuItem(
							id = "about",
							title = "About",
							contentDescription = "Get info about the developer",
							icon = Icons.Default.Info
						),
					),
					onItemClick = {}
				)
			},
			scaffoldState = scaffoldState
		) {
			Surface(
				modifier = Modifier.fillMaxSize(),
				color = MaterialTheme.colors.background
			) {
				Column {
					Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
						Column {
							if (!isConnected()) {
								TextField(
									value = output,
									onValueChange = {},
									readOnly = true,
									modifier = Modifier.fillMaxWidth()
								)
							}
							val layoutInfo = listState.layoutInfo

							var isAtBottom by remember {
								mutableStateOf(true)
							}
							if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
								val lastElem = layoutInfo.visibleItemsInfo[layoutInfo.visibleItemsInfo.lastIndex]
								isAtBottom = layoutInfo.viewportSize.height == lastElem.size + lastElem.offset
							}
							LazyColumn(state = listState, modifier = Modifier.onSizeChanged {

								if (size>it.height) {
									println("${it.height} $size")
									val scrollBy = (-it.height+size).toFloat()
									coroutineScope.launch {
										listState.animateScrollBy(scrollBy)
									}
								}
								size=it.height
							}) {
								synchronized(appendixList) {
									val sizeChanged = listSize != appendixList.size
									listSize = appendixList.size
									for (idx in appendixList.indices) {
										val text = appendixList[idx]
										item(idx, "Message") {
											text.compose()
										}
									}
									if (sizeChanged && isAtBottom) {
										coroutineScope.launch {
											if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
												listState.animateScrollToItem(
													listSize - 1,
													-layoutInfo.viewportSize.height + layoutInfo.visibleItemsInfo[layoutInfo.visibleItemsInfo.lastIndex].size
												)
											}
										}
									}
								}

							}
						}
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
								if (isConnected()) {
									Button(
										onClick = { Thread { reload() }.start() },
										modifier = Modifier
											.width(Dp(64f))
											.height(Dp(64f))
									) {
										Icon(
											imageVector = Icons.Default.Refresh,
											contentDescription = null,
											modifier = Modifier.size(42.dp),
											tint = LocalContentColor.current.copy(alpha = .8f)
										)
									}
								}
							}
						}

					}
					Row(
						verticalAlignment = Alignment.Bottom
					) {
						KeyboardOptions
						TextField(
							value = input,
							onValueChange = {
								input = it
							},
							modifier = Modifier
								.widthIn(
									min = LocalConfiguration.current.screenWidthDp.dp - Dp(64f),
									max = LocalConfiguration.current.screenWidthDp.dp - Dp(64f)
								)
								.heightIn(
									min = Dp(64f),
									max = LocalConfiguration.current.screenHeightDp.dp / 2f
								),
							shape = RectangleShape
						)
						Button(
							onClick = {
								inputs.add(input)
								input = ""
							},
							modifier = Modifier
								.height(Dp(64f))
								.background(
									TextFieldDefaults
										.textFieldColors()
										.backgroundColor(enabled = true).value
								),
							shape = RoundedCornerShape(64f)
						) {
							//Text("Send", fontSize = textSize)
							Icon(
								imageVector = Icons.Default.Send,
								contentDescription = null,
								modifier = Modifier.size(42.dp)
							)
						}
					}
				}
			}
		}
	}
}



@Preview
@Composable
fun DefaultPreview() {
	MyApplicationTheme {
		MainActivity().View(null, listOf())
	}
}
