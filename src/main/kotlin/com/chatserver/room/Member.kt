package com.chatserver.room

import io.ktor.http.cio.websocket.*

open class Member(
    val username: String,
    val sessionId: String,
    val socket: WebSocketSession
)