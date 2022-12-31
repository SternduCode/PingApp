package com.sterndu.pingonvolcano

enum class Type(val leftPad : Int, val rightPad : Int) {

    SYSTEM(32, 32), SEND(64, 0), RECEIVE(0, 64)

}