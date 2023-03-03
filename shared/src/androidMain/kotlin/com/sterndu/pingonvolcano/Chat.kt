package com.sterndu.pingonvolcano

class Chat(private var _name : String, private var _messages : MutableList<String> = mutableListOf()) {

	val name
		get() = _name

	val messages : List<String> = _messages.toList()

}
