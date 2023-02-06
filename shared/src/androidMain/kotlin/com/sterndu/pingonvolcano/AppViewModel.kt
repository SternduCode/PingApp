package com.sterndu.pingonvolcano

import android.net.LinkAddress
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AppViewModel : ViewModel() {

	init {
		start()
	}

	private lateinit var localIps: String
	private lateinit var globalIps: String
	private lateinit var configFile: File
	private lateinit var preferences: JsonObject

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

		preferences = Json.decodeFromStream(FileInputStream(configFile))
	}

	fun updatePreferences(preferences: JsonObject) {
		Json.encodeToStream(preferences, FileOutputStream(configFile))
	}

	private val _chats : MutableList<Chat> = mutableListOf()
	val chats : Sequence<Chat>
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
