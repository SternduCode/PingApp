package com.sterndu.pingonvolcano

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import com.sterndu.bridge.BridgeClient
import com.sterndu.bridge.BridgeUtil
import com.sterndu.bridge.HostConnector
import com.sterndu.data.transfer.Connector
import com.sterndu.data.transfer.SecureConnectionUtil
import com.sterndu.data.transfer.secure.ServerSocket
import com.sterndu.data.transfer.secure.Socket
import com.sterndu.multicore.*
import com.sterndu.util.interfaces.ThrowingRunnable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Runnable
import java.util.*


class AndroidPlatform : Platform {
	override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

var time : Long = 0L
var cons : ((String) -> Unit)? = null
val inputs = mutableListOf<String>()
var append : ((String, Type) -> Unit)? = null
var connected = false
var sock : com.sterndu.data.transfer.basic.Socket? = null

val run = {
	while (append == null) {
		Thread.sleep(5)
	}
	val append: (String, Type) -> Unit = append!! as (String, Type) -> Unit

	append.invoke("Do you want to host a connection? true/false", Type.SYSTEM)
	while (inputs.size == 0) {
		Thread.sleep(5)
	}
	val host = inputs.removeAt(0).equals("true", ignoreCase = true)
	append.invoke(host.toString(), Type.SYSTEM)

	append.invoke("Do you want to connect to a Bridge? true/false", Type.SYSTEM)
	while (inputs.size == 0) {
		Thread.sleep(5)
	}
	val bridge = inputs.removeAt(0).equals("true", ignoreCase = true)
	append.invoke(bridge.toString(), Type.SYSTEM)
	var server: String = ""
	var port: Int = BridgeUtil.DEFAULT_PORT;

	if (bridge) {
		append.invoke("Please enter the BridgeServer's address", Type.SYSTEM)
		while (inputs.size == 0) {
			Thread.sleep(5)
		}
		server = inputs.removeAt(0)
		append.invoke(server, Type.SYSTEM)

		append.invoke("Please enter the BridgeServer's port if it is non Default", Type.SYSTEM)
		while (inputs.size == 0) {
			Thread.sleep(5)
		}
		try {
			port = inputs.removeAt(0).toInt()
		} catch (e : NumberFormatException) {}
		append.invoke(port.toString(), Type.SYSTEM)
	}

	if (host) {
		if (bridge) {
			var bc : BridgeClient? = null
			try {
				bc = BridgeClient(server, port)
			} catch (e: Exception) {
				append.invoke("Failed to Connect to BridgeServer", Type.SYSTEM);
			}
			if (bc != null) {
				bridgeHost(bc, append, inputs)
			} else {
				reload()
			}
		} else {
			host(append, inputs)
		}
	} else {
		if (bridge) {
			append.invoke("Please enter the code you want to join ", Type.SYSTEM);
		} else {
			append.invoke("The IP you want to connect to: ", Type.SYSTEM);
		}
		while (inputs.size == 0) {
			Thread.sleep(5)
		}
		val host = inputs.removeAt(0)
		if (bridge) {
			var bc : BridgeClient? = null
			try {
				bc = BridgeClient(server, port)
			} catch (e: Exception) {
				append.invoke("Failed to Connect to BridgeServer", Type.SYSTEM);
			}
			if (bc != null) {
				bridgeClient(bc, append, inputs, host)
			} else {
				reload()
			}
		} else {
			client(host, append, inputs)
		}
	}
}

fun isConnected() = connected

fun reload() {
	if (sock != null && sock!!.isConnected && !sock!!.isClosed) {
		sock!!.sendData(
			1.toByte(),
			"Has left! Connection closed".toByteArray(charset("UTF-8"))
		)
		sock!!.sendClose()
		sock!!.close()
		sock = null
	}
	Updater.getInstance().remove("HandleWrittenMessages")
	connected = false
	Thread { run.invoke() }.start()
}

fun setConsumer(consumer: (String) -> Unit, _append: (String, Type) -> Unit) {
	cons = consumer
	append = _append
}

fun host(append: (String, Type) -> Unit, inputs: MutableList<String>) {
	try {
		SecureConnectionUtil.host(25566, true, false, 1) { cc, hc ->
			try {
				//cc.setShutdownHook(Messenger::stop);
				internalHost(cc, hc, append, inputs);
			} catch (e: IOException) {
				e.printStackTrace();
			}
		}
	} catch (e : IOException) {
		e.printStackTrace();
	}
}

fun internalHost(cc: Socket, hc: ServerSocket, append: (String, Type) -> Unit, inputs: MutableList<String>) {
	connected = true
	sock = cc
	append.invoke("Connected with " + cc.inetAddress, Type.SYSTEM)
	Updater.getInstance().add(ThrowingRunnable {
		if (inputs.size > 0) {
			val str: String = inputs.removeAt(0)
			append.invoke("You: $str", Type.SEND)
			if ("exit".equals(str, ignoreCase = true)) {
				reload()
			} else cc.sendData(1.toByte(), str.toByteArray(charset("UTF-8")))
		}
	}, "HandleWrittenMessages")
	cc.setHandle(1.toByte()) {
			type, data ->
		val str = String(data, charset("UTF-8"))
		append.invoke("Connection: $str", Type.RECEIVE)
	}
}

fun internalClient(cc: Socket, append: (String, Type) -> Unit, inputs: MutableList<String>) {
	connected = true
	sock = cc
	append.invoke("Connected to " + cc.inetAddress, Type.SYSTEM)
	Updater.getInstance().add(ThrowingRunnable {
		if (inputs.isNotEmpty()) {
			val str: String = inputs.removeAt(0)
			append.invoke("You: $str", Type.SEND)
			if ("exit".equals(str, ignoreCase = true)) {
				reload()
			} else {
				cc.sendData(1.toByte(), str.toByteArray(charset("UTF-8")))
			}
		}
	}, "HandleWrittenMessages")
	cc.setHandle(1.toByte()) {
			type, data ->
		val str = String(data, charset("UTF-8"))
		append.invoke("Connection: $str", Type.RECEIVE)
	}
}

fun bridgeHost(cc: BridgeClient, append: (String, Type) -> Unit, inputs: MutableList<String>) {
	val conn : HostConnector = cc.host()
	connected = true
	sock = conn.normalConnector.sock
	append.invoke("Connect via " + conn.code, Type.SYSTEM)
	Updater.getInstance().add(ThrowingRunnable {
		if (inputs.size > 0) {
			val str: String = inputs.removeAt(0)
			append.invoke("You: $str", Type.SEND)
			if ("exit".equals(str, ignoreCase = true)) {
				reload()
			} else {
				val prefix : ByteArray = byteArrayOf(
					0x00, 0x00, 0x00, 0x04, 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()
				)
				val dat : ByteArray = str.toByteArray(charset("UTF-8"))
				val send : ByteArray = prefix.plus(dat)
				conn.normalConnector.sendData(send)
			}
		}
	}, "HandleWrittenMessages")
	conn.announceConnector.setHandle() {
			type, data ->
		val str = String(data, charset("UTF-8"))
		append.invoke("Connected: $str", Type.SYSTEM)
	}
	conn.normalConnector.setHandle() {
			type, data ->
		val str = String(data, charset("UTF-8"))
		append.invoke("Connection: $str", Type.RECEIVE)
	}
}

fun bridgeClient(cc: BridgeClient, append: (String, Type) -> Unit, inputs: MutableList<String>, code : String) {
	val conn : Connector = cc.join(code)
	connected = true
	sock = conn.sock
	append.invoke("Connected to $code", Type.SYSTEM)
	Updater.getInstance().add(ThrowingRunnable {
		if (inputs.isNotEmpty()) {
			val str: String = inputs.removeAt(0)
			append.invoke("You: $str", Type.SEND)
			if ("exit".equals(str, ignoreCase = true)) {
				reload()
			} else {
				conn.sendData(str.toByteArray(charset("UTF-8")))
			}
		}
	}, "HandleWrittenMessages")
	conn.setHandle() {
			type, data ->
		val str = String(data, charset("UTF-8"))
		append.invoke("Connection: $str", Type.RECEIVE)
	}
}

fun client(host: String, append: (String, Type) -> Unit, inputs: MutableList<String>) {
	System.setProperty("debug", "true")
	try {
		SecureConnectionUtil.connect(host, 25566, true) { cc ->
			try {
				//cc.setShutdownHook(Messenger::stop)
				internalClient(cc, append, inputs)
			} catch (e : IOException) {
				e.printStackTrace();
			}
		}
	} catch (e : IOException) {
		append.invoke("Can't connect to $host", Type.SYSTEM);
		reload()
	}
}

fun getIps() = runBlocking {
	val client = HttpClient()
	val resp1 : HttpResponse? = try {
		client.get("https://ipv4.wtfismyip.com/text")
	} catch (e: Exception) {
		try {
			client.get("https://api4.ipify.org/?format=text")
		} catch (e : Exception) {
			null
		}
	}
	val resp2 : HttpResponse? = try {
		client.get("https://ipv6.wtfismyip.com/text")
	} catch (e: Exception) {
		try {
			client.get("https://api6.ipify.org/?format=text")
		} catch (e : Exception) {
			null
		}
	}
	return@runBlocking listOf(resp1?.body<String>()?.trim() ?: "",resp2?.body<String>()?.trim() ?: "")
}

@SuppressLint("SimpleDateFormat")
fun start() {
	reload()
	Updater.getInstance().add(Runnable {
		time = System.currentTimeMillis()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			cons?.invoke(SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy E z X").format(Date(time)))
		} else cons?.invoke("$time")
		val lConnected = connected
		connected = sock!=null && sock!!.isConnected && !sock!!.isClosed
		if (lConnected && !connected) {
			reload()
		}
	}, "Time",200)
}

fun cleanup() {
	Updater.getInstance().remove("Time")
	Updater.getInstance().remove("HandleWrittenMessages")
}

actual fun getPlatform(): Platform = AndroidPlatform()