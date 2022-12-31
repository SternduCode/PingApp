package com.sterndu.pingonvolcano

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform