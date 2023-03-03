package com.sterndu.pingonvolcano

import android.net.LinkAddress
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class AppViewModel : ViewModel() {

	init {
		start()
	}

	private lateinit var localIps: String
	private lateinit var globalIps: String
	private lateinit var configFile: File
	lateinit var preferences: MutableMap<String, JsonElement>

	operator fun MutableMap<String, JsonElement>.set(s: String, value: JsonElement) {
		this.put(s, value)
		println("Umhh $s $value")
		updatePreferences(this)
	}

	@OptIn(ExperimentalSerializationApi::class)
	fun init(localIps: List<LinkAddress>, globalIps: List<String>, configFile: File) {
		this.configFile = configFile
		this.localIps = localIps.joinToString(separator = "\n  ", prefix = "  ")
		this.globalIps = "Your Public IPs are:\n${
			globalIps.joinToString(
				separator = "\n  ",
				prefix = "  "
			)
		}"
		_status.update { "Your Local IPs are:\n$localIps\n$globalIps" }
		val map = Json.decodeFromStream(MapSerializer(String.serializer(), JsonElement.serializer()), FileInputStream(configFile))
		preferences = mutableMapOf()
		preferences.putAll(map)
	}

	@OptIn(ExperimentalSerializationApi::class)
	private fun updatePreferences(preferences: Map<String, JsonElement>) {
		Json.encodeToStream(JsonObject(preferences), FileOutputStream(configFile))
	}

	private val _chats : MutableMap<String, Chat> = mutableMapOf("default" to Chat("Default"))
	val chats : Sequence<Map.Entry<String, Chat>>
		get() = _chats.asSequence()

	private val _status = MutableStateFlow("")

	val status : StateFlow<String> = _status.asStateFlow()

	private val _elements : MutableList<Message> = mutableListOf()

	val elements : Sequence<Message>
		get() = _elements.asSequence()

	fun addChatMessage(message: Message) {
		synchronized(_elements) {
			_elements.add(message)
		}
	}

}
