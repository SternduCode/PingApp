package com.sterndu.pingonvolcano

import android.net.LinkAddress
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {

	init {
		start()
	}

	private lateinit var localIps: String
	private lateinit var globalIps: String

	fun init(localIps: List<LinkAddress>?, globalIps: List<String>) {
		this.localIps = ""
		this.localIps += localIps?.joinToString(separator = "\n  ", prefix = "  ") { "$it" }
		this.globalIps = "Your Public IPs are:\n${
			globalIps.joinToString(
				separator = "\n  ",
				prefix = "  "
			) { it }
		}"
	}

	fun updateTime(time: String) {
		_status.update { "$time\nYour Local IPs are:\n$localIps\n$globalIps" }
	}

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
