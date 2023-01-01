package com.sterndu.pingonvolcano.android

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.LinkAddress
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
				val navController = rememberNavController()

				val navHost = NavHost(navController = navController, startDestination = "chat") {
					composable("chat") { baseView(navController) { view(listLink, listGlobal) } }
					composable("about") { baseView(navController) { About().view() } }
					composable("settings") { /*Settings().view()*/ }
				}

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

	fun customShape(changeWidth : (Float) -> Unit) =  object : Shape {
		override fun createOutline(
			size: Size,
			layoutDirection: LayoutDirection,
			density: Density
		): Outline {
			changeWidth.invoke(size.width.coerceAtMost(size.height))
			return Outline.Rectangle(Rect(0f,0f,
				size.width.coerceAtMost(size.height) /* width */, size.height))
		}
	}

	@Composable
	@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
	fun baseView(navController: NavHostController, content: @Composable () -> Unit) {

		var drawerWidth by remember {
			mutableStateOf(0f)
		}

		val density = LocalDensity.current

		val scaffoldState = rememberScaffoldState()

		val coroutineScope = rememberCoroutineScope()

		Scaffold (
			drawerContent = {
				Drawerheader(with(density) { drawerWidth.toDp() })
				Drawerbody(
					drawerWidth = with(density) { drawerWidth.toDp() },
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
					onItemClick = {
						when(it.id) {
							"home" -> {
								navController.navigate("chat")
							}
							"settings" -> {
								navController.navigate("settings")
							}
							"about" -> {
								navController.navigate("about")
							}
						}
					}
				)
			},
			drawerShape = customShape { drawerWidth = it },
			scaffoldState = scaffoldState
		) {
			Surface(
				modifier = Modifier.fillMaxSize(),
				color = MaterialTheme.colors.background
			) {
				Box(contentAlignment = Alignment.TopEnd) {
					Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
						content.invoke()
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
							//println(navController.currentDestination?.navigatorName)
							if (isConnected() ) {
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
			}
		}
	}

	@Composable
	fun view(
		listLink: List<LinkAddress>?,
		listGlobal: List<String>
	) {
		var localIps: String = ""
		localIps += listLink?.joinToString(separator = "\n  ", prefix = "  ") { "$it" }
		val globalIps: String = "Your Public IPs are:\n${
			listGlobal.joinToString(
				separator = "\n  ",
				prefix = "  "
			) { "$it" }
		}"

		val appendixList : MutableList<Message> = remember { mutableStateListOf() }

		var listSize: Int = 0

		val coroutineScope = rememberCoroutineScope()

		var input by remember { mutableStateOf("") }
		var output by remember { mutableStateOf("") }

		val listState = rememberLazyListState()

		setConsumer({ inp ->
			output = "$inp\nYour Local IPs are:\n$localIps\n$globalIps"
		}) { text, type ->
			runOnUiThread {
				appendixList.add(Message(text, type))
			}
		}
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
							items(appendixList) {
								it.compose()
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


@Preview
@Composable
fun DefaultPreview() {
	MyApplicationTheme {
		MainActivity().view(null, listOf())
	}
}
